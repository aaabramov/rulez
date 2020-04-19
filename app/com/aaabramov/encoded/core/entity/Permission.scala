package com.aaabramov.encoded.core.entity

import java.time.LocalDateTime

import play.api.libs.json.{Json, OFormat}

case class Permission(
                       name: String,
                       description: String,
                       createdAt: LocalDateTime,
                       updatedAt: LocalDateTime,
                       active: Boolean = true,
                       id: Int = 0
                     )

object Permission {
  implicit val format: OFormat[Permission] = Json.format[Permission]

  val tupled = (this.apply _).tupled
}
