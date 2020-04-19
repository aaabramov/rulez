package com.aaabramov.encoded.core.entity

import java.time.LocalDateTime
import java.util.UUID

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OWrites}

case class User(
                 uuid: UUID,
                 email: String,
                 password: String,
                 roleId: Int,
                 createdAt: LocalDateTime,
                 updatedAt: LocalDateTime,
                 active: Boolean = true,
                 id: Int = 0
               ) {

  override def toString: String =
    copy(password = "***").asJsonString(User.writesAll)

}

object User {
  private val writesAll: OWrites[User] = Json.writes[User]

  implicit val format: OWrites[User] = writesAll.writes(_) - "password"

  val tupled = (this.apply _).tupled
}


