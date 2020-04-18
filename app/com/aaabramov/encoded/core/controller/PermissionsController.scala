package com.aaabramov.encoded.core.controller

import cats.instances.future._
import com.aaabramov.encoded.core.entity.Permission
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.impl.PermissionsRepository
import controllers.RestController
import javax.inject.Inject
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

case class CreatePermissionRequest(name: String, author: String /* TODO fetch from JWT header */)

object CreatePermissionRequest {
  implicit val format: OFormat[CreatePermissionRequest] = Json.format[CreatePermissionRequest]
}

case class UpdatePermissionRequest(name: String)

object UpdatePermissionRequest {
  implicit val format: OFormat[UpdatePermissionRequest] = Json.format[UpdatePermissionRequest]
}

class PermissionsController @Inject()(
                                       permissionsRepository: PermissionsRepository,
                                       now: Now
                                     )(implicit ec: ExecutionContext)
  extends RestController
  with CrudController[Int, CreatePermissionRequest, UpdatePermissionRequest, Permission] {

  override def create(): Action[CreatePermissionRequest] = Action.async(parse.json[CreatePermissionRequest]) { req =>
    val time = now()
    permissionsRepository
      .create {
        Permission(req.body.name, time, time)
      }
      .map(Created(_))
  }

  override def update(id: Int): Action[UpdatePermissionRequest] = Action.async(parse.json[UpdatePermissionRequest]) { req =>
    import req.body
    permissionsRepository
      .update(id)(_.copy(name = body.name))
      .map(Ok(_))
  }

  override def delete(id: Int): Action[AnyContent] = Action.async {
    permissionsRepository
      .delete(id)
      .map(Ok(_))
  }

  override def findOne(id: Int): Action[AnyContent] = Action.async {
    permissionsRepository
      .findOne(id)
      .map(Ok(_))
  }

  override def list(): Action[AnyContent] = Action.async {
    permissionsRepository
      .list()
      .map(Ok(_))
  }
}
