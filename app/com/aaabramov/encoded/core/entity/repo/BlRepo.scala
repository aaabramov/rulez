package com.aaabramov.encoded.core.entity.repo

import com.aaabramov.encoded.core.controller.RuleSetRequest
import com.aaabramov.encoded.core.entity.RuleSetEntity
import com.aaabramov.encoded.core.entity.bl.BlRepoImpl
import com.aaabramov.encoded.core.util.flow.FlowF
import com.google.inject.ImplementedBy
import slick.jdbc.PostgresProfile

@ImplementedBy(classOf[BlRepoImpl])
trait BlRepo {

  import PostgresProfile.api._
  import com.aaabramov.encoded.core.entity.bl.BlTables._

  def insert(req: RuleSetRequest): FlowF[RuleSetEntity]

  def update(id: Long, req: RuleSetRequest): FlowF[RuleSetEntity]

  def delete(id: Long): FlowF[Unit]

  def list(f: Option[RuleSetsTable => Rep[Boolean]] = None): FlowF[Seq[RuleSetEntity]]

  def findOne(id: Long): FlowF[RuleSetEntity]

}