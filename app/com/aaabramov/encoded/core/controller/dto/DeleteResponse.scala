package com.aaabramov.encoded.core.controller.dto

import play.api.libs.json.{Json, OFormat}

case class DeleteResponse(ok: Boolean, count: Int)

object DeleteResponse {
  implicit val format: OFormat[DeleteResponse] = Json.format[DeleteResponse]
}
