package com.aaabramov.encoded.core.controller.dto

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class KeyValue(key: String, value: String) {
  override def toString: String = this.asJsonString
}

object KeyValue {
  implicit val format: OFormat[KeyValue] = Json.format[KeyValue]
}