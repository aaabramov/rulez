package com.aaabramov.encoded.core.entity

import java.time.LocalDateTime

import play.api.libs.json.JsonConfiguration.Aux
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, OFormat}

case class SecureRequest(data: String, validTill: LocalDateTime, isOneTime: Option[Boolean])

object SecureRequest {
  private implicit val config: Aux[Json.MacroOptions] = JsonConfiguration(SnakeCase)
  implicit val format: OFormat[SecureRequest] = Json.format[SecureRequest]
}
