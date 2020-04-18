package com.aaabramov.encoded.core.entity.bl

import java.time.LocalDateTime

import com.aaabramov.encoded.core.entity.{Permission, Role, RolePermission}
import slick.jdbc.PostgresProfile

object BlTables {

  import PostgresProfile.api._

  val ruleSets = TableQuery[RuleSetsTable]
  val rules = TableQuery[RulesTable]
  val conditions = TableQuery[ConditionsTable]
  val roles = TableQuery[RolesTable]
  val permissions = TableQuery[PermissionsTable]
  val rolePermissions = TableQuery[RolePermissionsTable]

  class RuleSetsTable(tag: Tag) extends Table[RuleSet](tag, "rule_sets") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def author = column[String]("author")

    def createdAt = column[LocalDateTime]("created_at")

    def updatedAt = column[LocalDateTime]("updated_at")

    override def * = (name, author, createdAt, updatedAt, id).mapTo[RuleSet]
  }

  class RulesTable(tag: Tag) extends Table[Rule](tag, "rules") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def ruleSetId = column[Long]("rule_set_id")

    def key = column[String]("key")

    def value = column[String]("value")

    override def * = (ruleSetId, key, value, id).mapTo[Rule]
  }

  class ConditionsTable(tag: Tag) extends Table[Condition](tag, "conditions") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def ruleSetId = column[Long]("rule_set_id")

    def key = column[String]("key")

    def value = column[String]("value")

    override def * = (ruleSetId, key, value, id).mapTo[Condition]
  }

  class RolesTable(tag: Tag) extends Table[Role](tag, "roles") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    override def * = (name, id).mapTo[Role]
  }

  class PermissionsTable(tag: Tag) extends Table[Permission](tag, "permissions") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def createdAt = column[LocalDateTime]("created_at")

    def updatedAt = column[LocalDateTime]("updated_at")

    override def * = (name, createdAt, updatedAt, id).mapTo[Permission]
  }

  class RolePermissionsTable(tag: Tag) extends Table[RolePermission](tag, "role_permissions") {
    def roleId = column[Int]("role_id")

    def permissionId = column[Int]("permission_id")

    def pk = primaryKey("", (roleId, permissionId))

    override def * = (roleId, permissionId).mapTo[RolePermission]
  }

}