package com.aaabramov.encoded.core.entity

import play.api.libs.json.{Json, OFormat}

case class Permission(name: String, id: Long = 0L)

object Permission {
  implicit val format: OFormat[Permission] = Json.format[Permission]

  val tupled = (this.apply _).tupled
}
