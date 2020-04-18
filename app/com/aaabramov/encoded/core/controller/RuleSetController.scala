package com.aaabramov.encoded.core.controller

import cats.instances.future._
import com.aaabramov.encoded.core.entity.repo.BlRepo
import controllers.{JsonController, JsonErrorHandling}
import javax.inject.Inject
import play.api.Logging
import play.api.libs.json.{Json, OFormat}

import scala.concurrent.ExecutionContext

case class Entity(key: String, value: String)

case class KeyValue(
                     key: String,
                     value: String
                   )

object KeyValue {
  implicit val format: OFormat[KeyValue] = Json.format[KeyValue]
}

case class RuleSetRequest(name: String, author: String, rules: Set[KeyValue], conditions: Set[KeyValue])

object RuleSetRequest {
  implicit val format: OFormat[RuleSetRequest] = Json.format[RuleSetRequest]
}

class RuleSetController @Inject()(
                                   repo: BlRepo
                                 )(implicit ec: ExecutionContext)
  extends JsonController
  with Logging
  with JsonErrorHandling {

  def create = Action.async(parse.json[RuleSetRequest]) { req =>
    repo
      .insert(req.body)
      .map(Created(_))
  }

  def update(id: Long) = Action.async(parse.json[RuleSetRequest]) { req =>
    repo
      .update(id, req.body)
      .map(Created(_))
  }

  def delete(id: Long) = Action.async {
    repo
      .delete(id)
      .map(_ => Ok(Json.obj("ok" -> true)))
  }

  def list = Action.async {
    repo
      .list()
      .map(Ok(_))
  }

  def findOne(id: Long) = Action.async {
    repo
      .findOne(id)
      .map(Ok(_))
  }

}
