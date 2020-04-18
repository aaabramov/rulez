package com.aaabramov.encoded.core.entity.bl

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class Entry(ruleId: Long, key: String, value: String, id: Long = 0L) {
  override def toString: String = this.asJsonString
}

object Entry {
  implicit val format: OFormat[Entry] = Json.format[Entry]
  val tupled = (this.apply _).tupled
}
