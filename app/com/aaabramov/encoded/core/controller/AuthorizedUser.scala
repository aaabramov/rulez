package com.aaabramov.encoded.core.controller

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class AuthorizedUser(
                           email: String,
                           role: String,
                           permissions: Set[String]
                         ) {
  override def toString: String = this.asJsonString
}

object AuthorizedUser {
  implicit val format: OFormat[AuthorizedUser] = Json.format[AuthorizedUser]
}
