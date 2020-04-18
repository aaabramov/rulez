package com.aaabramov.encoded.core.entity

import java.time.LocalDateTime
import java.util.UUID

import play.api.libs.json.{Json, OFormat}

case class SecretEntity(
                         uuid: UUID,
                         data: String,
                         validTill: LocalDateTime,
                         isOneTime: Boolean,
                         viewed: Boolean,
                         id: Long = 0L
                       )

object SecretEntity {
  implicit val format: OFormat[SecretEntity] = Json.format[SecretEntity]

  val tupled = (this.apply _).tupled
}