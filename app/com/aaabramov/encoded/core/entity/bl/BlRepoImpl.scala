package com.aaabramov.encoded.core.entity.bl

import cats.instances.future._
import com.aaabramov.encoded.core.controller.dto.{KeyValue, RuleSetRequest}
import com.aaabramov.encoded.core.entity.RuleSetEntity
import com.aaabramov.encoded.core.entity.repo.BlRepo
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.liftAction
import com.aaabramov.encoded.core.util.error.CustomError
import com.aaabramov.encoded.core.util.flow.{Flow, FlowF}
import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.PostgresProfile

import scala.concurrent.ExecutionContext


class BlRepoImpl @Inject()(
                            override protected val dbConfigProvider: DatabaseConfigProvider,
                            now: Now
                          )
                          (implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[PostgresProfile]
  with BlRepo {

  import BlTables._
  import profile.api._

  private[this] val insertRuleSet = ruleSets.returning(ruleSets.map(_.id)).into((entity, id) => entity.copy(id = id))
  private[this] val insertRule = rules.returning(rules.map(_.id)).into((entity, id) => entity.copy(id = id))
  private[this] val insertCond = conditions.returning(conditions.map(_.id)).into((entity, id) => entity.copy(id = id))

  override def insert(req: RuleSetRequest): FlowF[RuleSetEntity] = {
    val at = now()
    val action = for {
      ruleSet <- insertRuleSet += RuleSet(req.name, req.author, at, at)
      insertedRules <- insertRules(ruleSet.id, req.rules.toSeq)
      insertedConds <- insertConds(ruleSet.id, req.conditions.toSeq)
    } yield RuleSetEntity(ruleSet, insertedRules, insertedConds)

    liftAction(db run action.transactionally)
  }

  private def insertRules(ruleSetId: Long, rules: Seq[KeyValue]) =
    insertRule ++= rules.map(r => Rule(ruleSetId, r.key, r.value))

  private def insertConds(ruleSetId: Long, conds: Seq[KeyValue]) =
    this.insertCond ++= conds.map(r => Condition(ruleSetId, r.key, r.value))

  override def update(id: Long, req: RuleSetRequest): FlowF[RuleSetEntity] = {

    val toUpdate = RuleSet(req.name, req.author, now(), now(), id = id) // FIXME

    val action = for {
      ruleSet <- ruleSets.filter(_.id === id).update(toUpdate)
      _ <- rules.filter(_.ruleSetId === id).delete
      _ <- conditions.filter(_.ruleSetId === id).delete
      insertedRules <- insertRules(id, req.rules.toSeq)
      insertedConds <- insertConds(id, req.conditions.toSeq)
    } yield RuleSetEntity(toUpdate, insertedRules, insertedConds)

    liftAction(db run action.transactionally)
  }


  override def delete(id: Long): FlowF[Unit] = {
    val action = for {
      _ <- ruleSets.filter(_.id === id).delete
      _ <- rules.filter(_.ruleSetId === id).delete
      _ <- conditions.filter(_.ruleSetId === id).delete
    } yield()

    liftAction(db run action.transactionally)
  }

  override def list(f: Option[RuleSetsTable => Rep[Boolean]] = None): FlowF[Seq[RuleSetEntity]] = {

    val query =
      ruleSets
        .filterOpt(f) { case (t, filter) => filter(t) }
        .join(conditions).on(_.id === _.ruleSetId)
        .join(rules).on { case ((rs, _), r) => rs.id === r.ruleSetId }

    liftAction(db run query.result)
      .map {
        _
          .map { case ((rs, c), r) =>
            println(s"$rs $c $r")
            (rs, c, r)
          }
          .groupBy { case (rs, _, _) => rs }
          .toSeq
          .map { case (rs, entries) =>
            RuleSetEntity(rs, entries.map(_._3).distinct, entries.map(_._2).distinct)
          }
      }

  }

  override def findOne(id: Long): FlowF[RuleSetEntity] =
    list {
      Some {
        _.id === id
      }
    }
      .flatMap { result =>
        if (result.nonEmpty)
          Flow.pure(result.head)
        else
          Flow.leftT(CustomError.entityNotFound("RuleSet", id.toString))

      }


}
