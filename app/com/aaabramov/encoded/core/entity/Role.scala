package com.aaabramov.encoded.core.entity

import play.api.libs.json.{Json, OFormat}

case class Role(name: String, id: Int = 0)

object Role {
  implicit val format: OFormat[Role] = Json.format[Role]

  val tupled = (this.apply _).tupled
}
