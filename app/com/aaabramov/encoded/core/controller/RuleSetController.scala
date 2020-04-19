package com.aaabramov.encoded.core.controller

import cats.instances.future._
import com.aaabramov.encoded.core.controller.dto.RuleSetRequest
import com.aaabramov.encoded.core.entity.repo.BlRepo
import controllers.{JsonController, JsonErrorHandling}
import javax.inject.Inject
import play.api.Logging
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

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
