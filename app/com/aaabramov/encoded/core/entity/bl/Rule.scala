package com.aaabramov.encoded.core.entity.bl

import java.time.LocalDateTime

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class Rule(ruleSetId: Long, key: String, value: String, id: Long = 0L) {
  override def toString: String = this.asJsonString
}

object Rule {
  implicit val format: OFormat[Rule] = Json.format[Rule]
  val tupled = (this.apply _).tupled
}
