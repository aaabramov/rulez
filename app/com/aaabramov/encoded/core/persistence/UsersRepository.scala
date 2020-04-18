package com.aaabramov.encoded.core.persistence

import java.util.UUID

import cats.instances.future._
import cats.instances.uuid
import com.aaabramov.encoded.core.entity.User
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.util.Mutation
import com.aaabramov.encoded.core.util.error.{CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.FlowF
import controllers.UserFilter
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class UsersRepository @Inject()(
                                 override protected val dbConfigProvider: DatabaseConfigProvider,
                                 now: Now
                               )
                               (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with Tables {

  import profile.api._

  private val insertQuery = users.returning(users.map(_.id)).into((entity, id) => entity.copy(id = id))

  def list(filter: Option[UserFilter]): FlowF[List[User]] = {
    val query = filter match {
      case Some(f) =>
        users
          .filterOpt(f.uuid)(_.uuid === _)
          .filterOpt(f.email)(_.email === _.toLowerCase)
      case None    => users
    }
    liftAction(db run query.result).map(_.toList)
  }

  def insert(user: User): FlowF[User] = {
    val action = insertQuery += user

    liftAction(db run action)
  }

  def findOne(filter: UserFilter): FlowF[User] = {
    val query = {
      users
        .filterOpt(filter.uuid)(_.uuid === _)
        .filterOpt(filter.email)(_.email === _.toLowerCase)
        .result.headOption
        .flatMap {
          case Some(found) => DBIO.successful(found)
          case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("User", uuid.toString)))
        }
    }

    liftAction(db run query)
  }

  def update(uuid: UUID)(mutation: Mutation[User]): FlowF[User] = {
    val action = for {
      maybeOne <- users.filter(_.uuid === uuid).result.headOption
      action <- maybeOne match {
        case Some(found) =>
          val updated = mutation(found).copy(updatedAt = now())
          users
            .filter(_.uuid === uuid)
            .update(updated)
            .map(_ => updated)
        case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("User", uuid.toString)))
      }
    } yield action

    liftAction(db run action.transactionally)
  }

}


