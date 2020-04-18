package com.aaabramov.encoded.core.controller.dto

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class RuleSetRequest(
                           name: String,
                           author: String,
                           rules: Set[KeyValue],
                           conditions: Set[KeyValue]
                         ) {
  override def toString: String = this.asJsonString
}

object RuleSetRequest {
  implicit val format: OFormat[RuleSetRequest] = Json.format[RuleSetRequest]
}