package com.aaabramov.encoded.core.persistence.impl

import cats.instances.future._
import com.aaabramov.encoded.core.controller.dto.DeleteResponse
import com.aaabramov.encoded.core.entity.Permission
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.{CrudRepository, liftAction}
import com.aaabramov.encoded.core.util.Mutation
import com.aaabramov.encoded.core.util.error.{ApiMessage, CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.FlowF
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext

class PermissionsRepository @Inject()(
                                       override protected val dbConfigProvider: DatabaseConfigProvider,
                                       now: Now
                                     )(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with CrudRepository[Int, Permission] {

  import com.aaabramov.encoded.core.entity.bl.BlTables._
  import profile.api._

  private val insertQuery = permissions.returning(permissions.map(_.id)).into((entity, id) => entity.copy(id = id))

  override def create(entity: Permission): FlowF[Permission] = {
    val action = permissions
      .filter(_.name === entity.name)
      .result.headOption
      .flatMap {
        case Some(_) => DBIO.failed(ExceptionError(CustomError.internal(ApiMessage(s"Permission with name=${entity.name} already exists"))))
        case _       =>
          insertQuery += entity
      }

    liftAction(db run action)
  }

  override def update(id: Int)(mutation: Mutation[Permission]): FlowF[Permission] = {
    val action = for {
      maybeOne <- permissions.filter(_.id === id).result.headOption
      action <- maybeOne match {
        case Some(found) =>
          val updated = mutation(found).copy(updatedAt = now())
          permissions
            .filter(_.id === id)
            .update(updated)
            .map(_ => updated)
        case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("Permission", id.toString)))
      }
    } yield action

    liftAction(db run action.transactionally)
  }

  override def delete(id: Int): FlowF[DeleteResponse] = {
    val action =
      permissions
        .filter(_.id === id)
        .delete
        .map(count => DeleteResponse(ok = true, count = count))

    liftAction(db run action)
  }

  override def findOne(id: Int): FlowF[Permission] = {
    val query =
      permissions
        .filter(_.id === id)
        .result.headOption
        .flatMap {
          case Some(found) => DBIO.successful(found)
          case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("Permission", id.toString)))
        }

    liftAction(db run query)
  }

  override def list(): FlowF[List[Permission]] = {
    val query = permissions.result

    liftAction(db run query)
      .map(_.toList)
  }

}


