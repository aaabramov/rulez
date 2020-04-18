package com.aaabramov.encoded.core.entity

import com.aaabramov.encoded.core.entity.bl.{Condition, Rule, RuleSet}
import play.api.libs.json.{Json, OFormat}

case class RuleSetEntity(ruleSet: RuleSet, rules: Seq[Rule], conditions: Seq[Condition])

object RuleSetEntity {
  implicit val format: OFormat[RuleSetEntity] = Json.format[RuleSetEntity]
}