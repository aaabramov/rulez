package com.aaabramov.encoded.core.controller

import cats.instances.future._
import com.aaabramov.encoded.core.controller.PermissionsController._
import com.aaabramov.encoded.core.entity.Permission
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.impl.{AuthRepository, PermissionsRepository}
import com.aaabramov.encoded.core.util.json.AnyAsJson
import controllers.RestController
import javax.inject.Inject
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

class PermissionsController @Inject()(
                                       permissionsRepository: PermissionsRepository,
                                       authRepository: AuthRepository,
                                       now: Now
                                     )(implicit ec: ExecutionContext)
  extends RestController
  with CrudController[Int, CreatePermissionRequest, UpdatePermissionRequest, Permission] {

  override def create(): Action[CreatePermissionRequest] = Action.async(parse.json[CreatePermissionRequest]) { req =>
    import req.body
    val time = now()
    permissionsRepository
      .create {
        Permission(body.name, body.description, time, time)
      }
      .map(Created(_))
  }

  override def update(id: Int): Action[UpdatePermissionRequest] = Action.async(parse.json[UpdatePermissionRequest]) { req =>
    import req.body
    permissionsRepository
      .update(id)(_.copy(name = body.name, description = body.description))
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

  def enable(id: Int) = Action.async {
    permissionsRepository
      .update(id)(_.copy(active = true))
      .map(Ok(_))
  }

  def disable(id: Int) = Action.async {
    permissionsRepository
      .update(id)(_.copy(active = false))
      .map(Ok(_))
  }

  def users(id: Int) = Action.async {
    authRepository
      .findUsersWithPermission(id)
      .map {
        _
          .groupBy { case (_, r) => r }
          .mapValues(_.map { case (u, _) => u })
          .map { case (role, users) =>
            Json.obj("role" -> role, "users" -> users.asJson)
          }
      }
      .map(Ok(_))
  }

}

object PermissionsController {

  case class CreatePermissionRequest(name: String, description: String, author: String /* TODO fetch from JWT header */)

  object CreatePermissionRequest {
    implicit val format: OFormat[CreatePermissionRequest] = Json.format[CreatePermissionRequest]
  }

  case class UpdatePermissionRequest(name: String, description: String)

  object UpdatePermissionRequest {
    implicit val format: OFormat[UpdatePermissionRequest] = Json.format[UpdatePermissionRequest]
  }

}
