package com.aaabramov.encoded.core.controller

import cats.instances.future._
import com.aaabramov.encoded.core.controller.RolesController._
import com.aaabramov.encoded.core.entity.Role
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.impl.{AuthRepository, RolesRepository}
import com.aaabramov.encoded.core.util.json.AnyAsJson
import controllers.RestController
import javax.inject.Inject
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent}

import scala.concurrent.ExecutionContext

class RolesController @Inject()(
                                 rolesRepository: RolesRepository,
                                 authRepository: AuthRepository,
                                 now: Now
                               )(implicit ec: ExecutionContext)
  extends RestController
  with CrudController[Int, CreateRoleRequest, UpdateRoleRequest, Role] {

  override def create(): Action[CreateRoleRequest] = Action.async(parse.json[CreateRoleRequest]) { req =>
    import req.body
    val time = now()
    rolesRepository
      .create {
        Role(body.name, body.description, time, time)
      }
      .map(Created(_))
  }

  override def update(id: Int): Action[UpdateRoleRequest] = Action.async(parse.json[UpdateRoleRequest]) { req =>
    import req.body
    rolesRepository
      .update(id)(_.copy(name = body.name, description = body.description))
      .map(Ok(_))
  }

  override def delete(id: Int): Action[AnyContent] = Action.async {
    rolesRepository
      .delete(id)
      .map(Ok(_))
  }

  override def findOne(id: Int): Action[AnyContent] = Action.async { req =>
    val result = if (req.getQueryString("withPermissions").contains("true")) {
      rolesRepository
        .findWithPermissions(id)
        .map { case (role, permissions) =>
          role.asJsonObject + ("permissions" -> permissions.asJson)
        }
    } else {
      rolesRepository
        .findOne(id)
        .map(_.asJson)
    }

    result.map(Ok(_))
  }

  override def list(): Action[AnyContent] = Action.async {
    rolesRepository
      .list()
      .map(Ok(_))
  }

  def enable(id: Int) = Action.async {
    rolesRepository
      .update(id)(_.copy(active = true))
      .map(Ok(_))
  }

  def disable(id: Int) = Action.async {
    rolesRepository
      .update(id)(_.copy(active = false))
      .map(Ok(_))
  }

  def users(id: Int) = Action.async {
    authRepository
      .findUsersWithRole(id)
      .map { case (role, users) =>
        Json.obj(
          "role" -> role,
          "users" -> users.asJson
        )
      }
      .map(Ok(_))
  }

}

object RolesController {

  case class CreateRoleRequest(name: String, description: String, author: String /* TODO fetch from JWT header */)

  object CreateRoleRequest {
    implicit val format: OFormat[CreateRoleRequest] = Json.format[CreateRoleRequest]
  }

  case class UpdateRoleRequest(name: String, description: String)

  object UpdateRoleRequest {
    implicit val format: OFormat[UpdateRoleRequest] = Json.format[UpdateRoleRequest]
  }

}
