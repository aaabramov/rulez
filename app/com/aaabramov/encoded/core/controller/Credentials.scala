package com.aaabramov.encoded.core.controller

import play.api.libs.json.{Json, OFormat}

case class Credentials(email: String, password: String)

object Credentials {
  implicit val format: OFormat[Credentials] = Json.format[Credentials]
}