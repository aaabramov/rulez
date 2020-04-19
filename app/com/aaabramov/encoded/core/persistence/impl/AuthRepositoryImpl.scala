package com.aaabramov.encoded.core.persistence.impl

import cats.instances.future._
import com.aaabramov.encoded.core.controller.{AuthorizedUser, Credentials}
import com.aaabramov.encoded.core.entity.{Role, User}
import com.aaabramov.encoded.core.persistence.{Tables, liftAction}
import com.aaabramov.encoded.core.util.error.{CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.{Flow, FlowF}
import com.github.t3hnar.bcrypt._
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class AuthRepositoryImpl @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider)
                                  (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with AuthRepository
  with Tables {

  import PostgresProfile.api._
  import com.aaabramov.encoded.core.entity.bl.BlTables._

  def auth(credentials: Credentials): FlowF[AuthorizedUser] = {

    val query = for {
      //@formatter:off
      maybeUser   <- users
        .filter(_.email === credentials.email.toLowerCase)
        .result.headOption
      user        <- validateUser(maybeUser, credentials)
      role        <- roles.filter(_.id === user.roleId).result.head
      permissions <- rolePermissions
        .filter(_.roleId === role.id)
        .join(permissions).on(_.permissionId === _.id)
        .filter { case (_, p) => p.active }
        .result
        .map(_.map { case (_, p) => p })
      //@formatter:on
    } yield AuthorizedUser(user.email, role.name, permissions.map(_.name).toSet)

    liftAction(db run query.withPinnedSession)
  }

  private def validateUser(maybeUser: Option[User], credentials: Credentials) =
    maybeUser match {
      case Some(user) =>
        if (!user.active)
          DBIO.failed(ExceptionError(
            CustomError.free(
              apiMessage = "Wrong email or password",
              internalMessage = "Use has been disabled",
              httpCode = 401
            )
          ))
        else if (credentials.password.isBcrypted(user.password))
          DBIO.successful(user)
        else
          DBIO.failed(ExceptionError(
            CustomError.free(
              apiMessage = "Wrong email or password",
              internalMessage = "Invalid password",
              httpCode = 401
            )
          ))

      case None =>
        DBIO.failed(ExceptionError(
          CustomError.free(
            apiMessage = "Wrong email or password",
            internalMessage = s"User with email=${credentials.email} does not exit",
            httpCode = 401
          )
        ))
    }

  override def findDefault(): FlowF[Role] = {

    val query =
      roles
        .filter(_.name === "default")
        .result.headOption

    liftAction(db run query)
      .flatMap {
        case Some(role) => Flow.pure(role)
        case _          => Flow.leftT(CustomError.internal("Cannot file default role"))
      }

  }


  override def findUsersWithRole(id: Int): FlowF[(Role, List[User])] = {

    val query = for {
      role <- roles.filter(_.id === id).result.head
      users <- users.filter(_.roleId === id).result
    } yield (role, users.toList)

    liftAction(db run query)
  }

  override def findUsersWithPermission(id: Int): FlowF[List[(User, Role)]] = {

    val query = rolePermissions
      .filter(_.permissionId === id)
      .join(roles).on(_.roleId === _.id)
      .join(users).on { case ((_, r), u) => u.roleId === r.id }
      .map { case ((_, r), u) => (u, r) }

    liftAction(db run query.result)
      .map(_.toList)
  }

}


