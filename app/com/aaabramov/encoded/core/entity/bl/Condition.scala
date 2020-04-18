package com.aaabramov.encoded.core.entity.bl

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class Condition(ruleSetId: Long, key: String, value: String, id: Long = 0L) {
  override def toString: String = this.asJsonString
}

object Condition {
  implicit val format: OFormat[Condition] = Json.format[Condition]
  val tupled = (this.apply _).tupled
}
