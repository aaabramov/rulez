package com.aaabramov.encoded.core.entity

import play.api.libs.json.{Json, OFormat}

case class RolePermission(roleId: Long, permissionId: Long)

object RolePermission {
  implicit val format: OFormat[RolePermission] = Json.format[RolePermission]

  val tupled = (this.apply _).tupled
}
