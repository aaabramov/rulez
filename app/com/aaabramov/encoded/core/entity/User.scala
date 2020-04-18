package com.aaabramov.encoded.core.entity

import java.time.LocalDateTime
import java.util.UUID

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.JsonConfiguration.Aux
import play.api.libs.json.JsonNaming.SnakeCase
import play.api.libs.json.{Json, JsonConfiguration, OFormat}

case class User(
                 uuid: UUID,
                 email: String,
                 password: String,
                 roleId: Long,
                 createdAt: LocalDateTime,
                 updatedAt: LocalDateTime,
                 id: Int = 0
               ) {

  override def toString: String =
    copy(password = "***").asJsonString

}

object User {
  private implicit val config: Aux[Json.MacroOptions] = JsonConfiguration(SnakeCase)
  implicit val format: OFormat[User] = Json.format[User]

  val tupled = (this.apply _).tupled
}


