/*
 * Copyright (c) TRANZZO LTD - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.aaabramov.encoded.core.util.flow

import cats.data.EitherT
import cats.{Applicative, Functor}
import com.aaabramov.encoded.core.util.error.CustomError

import scala.language.higherKinds

object Flow {

  /**
   * Create Flow[F, A] from `F[Either[CustomError, A]]`
   * Example:
   * {{{
   *   val right: Id[Either[CustomError, String]] = Right("Joe")
   *   Flow(right)
   *
   *   val left: Id[Either[CustomError, String]] = Left(InternalError(ApiMessage("WTF?")))
   *   Flow(left)
   * }}}
   */
  def apply[F[_], A](value: F[Either[CustomError, A]]): Flow[F, A] = EitherT(value)

  /**
   * Create Flow[F, A] from A.
   * Example:
   * {{{
   *   val f: Int = 10
   *   val flow: Flow[Id, Int] = Flow.pure(f)
   * }}}
   */
  def pure[F[_] : Applicative, A](value: A): Flow[F, A] = EitherT.pure[F, CustomError](value)

  /**
   * Create Flow[F, A] from F[A].
   * Example:
   * {{{
   *   val f: Id[Int] = 10
   *   val flow: Flow[Id, Int] = Flow.liftF(f)
   * }}}
   */
  def liftF[F[_] : Functor, A](value: F[A]): Flow[F, A] = EitherT.liftF(value)

  /**
   * Create left-biased Flow[F, A] from F[CustomError].
   * Example:
   * {{{
   *   val f: Id[CustomError] = CustomError(ApiMessage("Got an error))
   *   val flow: Flow[Id, Int] = Flow.left(f)
   * }}}
   */
  def left[F[_] : Functor, A](value: F[CustomError]): Flow[F, A] = EitherT.left(value)

  /**
   * Create left-biased Flow[F, A] from CustomError.
   * Example:
   * {{{
   *   val error: CustomError = InternalError("DatabaseUnreachable", "InternalError", Some("Contact technical support"))
   *   val flow: Flow[Id, Int] = Flow.leftT(error)
   * }}}
   */
  def leftT[F[_] : Applicative, B](error: CustomError): Flow[F, B] = EitherT.leftT[F, B](error)

  /**
   * Create right-biased Flow[F, A] from CustomError.
   * Example:
   * {{{
   *   val f: Int = 10
   *   val flow: Flow[Id, Int] = Flow.rightT(f)
   * }}}
   */
  def rightT[F[_] : Applicative, A](value: A): Flow[F, A] = EitherT.rightT(value)

  /**
   * Depending on condition `test` return corresponding [[Flow]].
   *
   * Example:
   * {{{
   *   val test = true
   *   val flow: Flow[Id, Int] = Flow.cond(test, 10, InternalError("DatabaseUnreachable", "InternalError", Some("Contact technical support")))
   * }}}
   */
  def cond[F[_] : Applicative, A](test: Boolean, right: => A, left: => CustomError): Flow[F, A] =
    EitherT.cond(test, right, left)

  /**
   * Depending on condition `test` return corresponding [[Flow]].
   *
   * Example:
   * {{{
   *   val test = true
   *   val right: Id[Int] = 10
   *   val flow: Flow[Id, Int] = Flow.condF(test, right, InternalError("DatabaseUnreachable", "InternalError", Some("Contact technical support")))
   * }}}
   */
  def condF[F[_] : Applicative, A](test: Boolean, right: => F[A], left: => CustomError): Flow[F, A] =
    if (test) Flow.liftF(right) else Flow.leftT(left)

  /**
   * Returns [[Unit]] boxed into [[Flow]].
   *
   * Example:
   *
   * {{{
   *   val flow: Flow[Id, Unit] = Flow.unit
   * }}}
   */
  def unit[F[_] : Applicative]: Flow[F, Unit] = Flow.pure(())

  /**
   * Create left-biased Flow[F, A] for Some or right-based Flow[F, A] for None.
   * Example:
   * {{{
   *   val some: Option[Int] = Some(1)
   *   val none: Option[Int] = None
   *
   *   val flow1: Flow[Id, Int] = Flow.fromOption(some)
   *   val flow2: Flow[Id, Int] = Flow.fromOption(none)
   * }}}
   */
  def fromOption[F[_] : Applicative, A](opt: Option[A], ifNone: => CustomError): Flow[F, A] =
    EitherT.fromOption(opt, ifNone)

  /**
   * Create left-biased Flow[F, A] for non empty F[Option] or right-based Flow[F, A] for empty F[Option].
   * Example:
   * {{{
   *   val some: Id[Option[Int]] = Some(1)
   *   val none: Id[Option[Int]] = None
   *
   *   val flow1: Flow[Id, Int] = Flow.fromOptionF(some)
   *   val flow2: Flow[Id, Int] = Flow.fromOptionF(none)
   * }}}
   */
  def fromOptionF[F[_] : Applicative, A](opt: F[Option[A]], ifNone: => CustomError): Flow[F, A] =
    EitherT.fromOptionF(opt, ifNone)

  /**
   * Create left-biased Flow[F, A] for left-based Either or right-based Flow[F, A] for right-based Either.
   * Example:
   * {{{
   *   val some: Id[Either[CustomError, Int]] = Right(1)
   *   val none: Id[Either[CustomError, Int]] = Left(ValidationError(""))
   *
   *   val flow1: Flow[Id, Int] = Flow.fromEither(some)
   *   val flow2: Flow[Id, Int] = Flow.fromEither(none)
   * }}}
   */
  def fromEither[F[_] : Applicative, A](either: Either[CustomError, A]): Flow[F, A] =
    EitherT.fromEither(either)

}