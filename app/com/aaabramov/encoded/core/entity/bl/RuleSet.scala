package com.aaabramov.encoded.core.entity.bl

import java.time.LocalDateTime

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

case class RuleSet(name: String, author: String, createdAt: LocalDateTime, updatedAt: LocalDateTime, id: Long = 0L) {
  override def toString: String = this.asJsonString
}

object RuleSet {
  implicit val format: OFormat[RuleSet] = Json.format[RuleSet]
  val tupled = (this.apply _).tupled
}
