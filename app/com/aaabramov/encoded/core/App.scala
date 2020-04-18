package com.aaabramov.encoded.core

sealed trait Result {
  final def &(that: Result): Result = this match {
    case Ok   => that
    case NeOk => NeOk
  }
}

object Result {
  def apply(b: Boolean): Result = if (b) Ok else NeOk
}

case object Ok extends Result

case object NeOk extends Result

case class Context(ctx: Map[String, String])

case class Rule(conditions: Seq[Context => Boolean], rule: Input => Boolean) {
  def check(input: Input): Result = {
    val shouldCheck = conditions.forall(_.apply(input.ctx))
    if (shouldCheck)
      Result(!rule(input))
    else
      Ok
  }
}

case class Input(ctx: Context, data: Map[String, String])

case class Rules(rules: Seq[Rule])

object App extends scala.App {

  val input = Input(
    ctx = Context(Map(
      "name" -> "Joe"
    )),
    data = Map(
      "age" -> "20",
      "secret" -> "password"
    )
  )

  val rules = Rules(Seq(
    Rule(
      conditions = Seq(
        _.ctx.get("name").forall(_ == "Joe")
      ),
      _.data.get("age").forall(_ == "20")
    )
  ))

  def check(input: Input, against: Rules): Result = {
    val res = against
      .rules
      .map(
        _.check(input)
      )
    Result(res.forall(_ == Ok))
  }

  println {
    check(input, rules)
  }

}
