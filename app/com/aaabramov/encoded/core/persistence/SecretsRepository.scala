package com.aaabramov.encoded.core.persistence

import java.util.UUID

import com.aaabramov.encoded.core.entity.{SecretEntity, SecureRequest}
import com.aaabramov.encoded.core.util.Mutation
import com.aaabramov.encoded.core.util.error.{CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.{Flow, FlowF}
import controllers.SecureFilter
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.{ExecutionContext, Future}


class SecretsRepository @Inject()(override protected val dbConfigProvider: DatabaseConfigProvider)
                                 (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with Tables {

  import profile.api._

  private val insertQuery = secrets.returning(secrets.map(_.id)).into((entity, id) => entity.copy(id = id))

  def list(filter: Option[SecureFilter]): FlowF[Seq[SecretEntity]] = {
    val query = filter match {
      case Some(f) =>
        secrets.filterOpt(f.uuid)(_.uuid === _)
      case None    => secrets
    }
    liftAction(db run query.result)
  }

  def insert(request: SecureRequest): FlowF[SecretEntity] = {
    val action = insertQuery += SecretEntity(UUID.randomUUID(), request.data, request.validTill, request.isOneTime.getOrElse(true), false)

    liftAction(db run action)
  }

  def findOne(uuid: UUID): FlowF[SecretEntity] = {
    val query = {
      secrets
        .filter(_.uuid === uuid)
        .result.headOption
        .flatMap {
          case Some(found) => DBIO.successful(found)
          case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("SecretEntity", uuid.toString)))
        }
    }

    liftAction(db run query)
  }

  def update(uuid: UUID)(mutation: Mutation[SecretEntity]): FlowF[SecretEntity] = {
    val action = for {
      maybeOne <- secrets.filter(_.uuid === uuid).result.headOption
      action <- maybeOne match {
        case Some(found) =>
          val updated = mutation(found)
          secrets
            .filter(_.uuid === uuid)
            .update(mutation(found))
            .map(_ => updated)
        case _           => DBIO.failed(ExceptionError(CustomError.entityNotFound("SecretEntity", uuid.toString)))
      }
    } yield action

    liftAction(db run action.transactionally)
  }

}


