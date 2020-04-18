package com.aaabramov.encoded.core.controller

import play.api.libs.json.{Json, OFormat}

case class AuthResponse(ok: Boolean, error: Option[String] = None, jwt: Option[String] = None) {
  require(Set(error, jwt).count(_.isDefined) == 1, "You can provide error or JWT")
}

object AuthResponse {
  implicit val format: OFormat[AuthResponse] = Json.format[AuthResponse]
}