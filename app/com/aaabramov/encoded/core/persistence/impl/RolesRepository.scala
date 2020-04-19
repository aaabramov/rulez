package com.aaabramov.encoded.core.persistence.impl

import cats.instances.future._
import com.aaabramov.encoded.core.controller.dto.DeleteResponse
import com.aaabramov.encoded.core.entity.{Permission, Role}
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.{CrudRepository, liftAction}
import com.aaabramov.encoded.core.util.Mutation
import com.aaabramov.encoded.core.util.error.{ApiMessage, CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.FlowF
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class RolesRepository @Inject()(
                                 override protected val dbConfigProvider: DatabaseConfigProvider,
                                 now: Now
                               )(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with CrudRepository[Int, Role] {

  import com.aaabramov.encoded.core.entity.bl.BlTables._
  import profile.api._

  private val insertQuery = roles.returning(roles.map(_.id)).into((entity, id) => entity.copy(id = id))

  override def create(entity: Role): FlowF[Role] = {
    val action = roles
      .filter(_.name === entity.name)
      .result.headOption
      .flatMap {
        case Some(_) => DBIO.failed(ExceptionError(CustomError.internal(ApiMessage(s"Role with name=${entity.name} already exists"))))
        case _       =>
          insertQuery += entity
      }

    liftAction(db run action)
  }

  override def update(id: Int)(mutation: Mutation[Role]): FlowF[Role] = {
    val action = for {
      maybeOne <- roles.filter(_.id === id).result.headOption
      action <- maybeOne match {
        case Some(found) =>
          val updated = mutation(found).copy(updatedAt = now())
          roles
            .filter(_.id === id)
            .update(updated)
            .map(_ => updated)
        case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("Role", id.toString)))
      }
    } yield action

    liftAction(db run action.transactionally)
  }

  override def delete(id: Int): FlowF[DeleteResponse] = {
    val action =
      roles
        .filter(_.id === id)
        .delete
        .map(count => DeleteResponse(ok = true, count = count))

    liftAction(db run action)
  }

  override def findOne(id: Int): FlowF[Role] = {
    val query =
      roles
        .filter(_.id === id)
        .result.headOption
        .flatMap {
          case Some(found) => DBIO.successful(found)
          case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("Role", id.toString)))
        }

    liftAction(db run query)
  }

  def findWithPermissions(id: Int): FlowF[(Role, Set[Permission])] = {
    val query = for {
      //@formatter:off
      role        <- roles.filter(_.id === id).result.head
      permissions <- rolePermissions
                       .filter(_.roleId === id)
                       .join(permissions).on(_.permissionId === _.id)
                       .map { case(_, p) => p }
                       .result
      //@formatter:on
    } yield (role, permissions.toSet)

    liftAction(db run query.withPinnedSession)
  }

  override def list(): FlowF[List[Role]] = {
    val query = roles.result

    liftAction(db run query)
      .map(_.toList)
  }

}


