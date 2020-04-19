package com.aaabramov.encoded.core.persistence

import java.time.LocalDateTime
import java.util.UUID

import com.aaabramov.encoded.core.entity.{SecretEntity, User}
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.PostgresProfile

trait Tables { this: HasDatabaseConfigProvider[PostgresProfile] =>

  import profile.api._

  protected val secrets = TableQuery[SecretsTable]
  protected val users = TableQuery[UsersTable]

  class SecretsTable(tag: Tag) extends Table[SecretEntity](tag, "secrets") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def data = column[String]("data", O.Unique)

    def uuid = column[UUID]("uuid")

    def validTill = column[LocalDateTime]("valid_till")

    def isOneTime = column[Boolean]("is_one_time", O.Default(false))

    def viewed = column[Boolean]("viewed", O.Default(false))

    override def * = (uuid, data, validTill, isOneTime, viewed, id).mapTo[SecretEntity]
  }

  class UsersTable(tag: Tag) extends Table[User](tag, "users") {
    def uuid = column[UUID]("uuid", O.Unique)

    def email = column[String]("email", O.Unique)

    def roleId = column[Int]("role_id")

    def password = column[String]("password")

    def createdAt = column[LocalDateTime]("created_at")

    def updatedAt = column[LocalDateTime]("updated_at")

    def active = column[Boolean]("active")

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    override def * = (
      uuid,
      email,
      password,
      roleId,
      createdAt,
      updatedAt,
      active,
      id
      ).mapTo[User]
  }

}
