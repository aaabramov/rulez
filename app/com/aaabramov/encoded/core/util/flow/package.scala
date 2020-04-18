package com.aaabramov.encoded.core.util

import cats.Monad
import cats.data.EitherT
import com.aaabramov.encoded.core.util.error.CustomError

import scala.concurrent.Future
import scala.language.higherKinds

package object flow {

  type Flow[F[_], A] = EitherT[F, CustomError, A]
  type FlowF[A] = EitherT[Future, CustomError, A]

  implicit class FlowExt[F[_], A](val self: Flow[F, A]) extends AnyVal {

    /**
     * Filter the `Flow`. If condition failed return failed `Flow` with specified error factory.
     * Example:
     * {{{
     *   case class User(name: String, age: Int)
     *
     *   class AdultService[F[_]: Monad] {
     *
     *     def register(user: User): Flow[F, User] =
     *       Flow
     *         .pure(user)
     *         .filter(_.age >= 18, TooYoungError(ApiMessage("Try again later.")))
     *         .flatMap(save)
     *
     *     private def save(user: User): Flow[F, User] = ???
     *
     *   }
     * }}}
     *
     */
    def filter(test: A => Boolean, onFail: A => CustomError)(implicit M: Monad[F]): Flow[F, A] =
      self.flatMap(value => if (test(value)) Flow.pure(value) else Flow.leftT(onFail(value)))

    /**
     * Applies asynchronous handler to `Flow` pipeline.
     *
     * Example:
     * {{{
     *   class FileService[F[_]] {
     *
     *     def upload(name: String, content: Array[Byte]): Flow[F, Boolean] =
     *       doUpload(name, content)
     *         .guarantee {
     *           case Right(success) => logger.debug(s"Successfully uploaded file $name")
     *           case Left(error)    => logger.error(s"Failed to upload file $name: $error")
     *         }
     *
     *     private def doUpload(name: String, content: Array[Byte]): Flow[F, Unit] = ???
     *
     *   }
     * }}}
     *
     * @note `pf` is a side effect function and is launched asynchronously in `Flow` pipeline
     * @param pf a `PartialFunction` which will be conditionally applied to the outcome of this `Flow`
     * @return original `Flow`
     */
    def guarantee(pf: PartialFunction[Either[CustomError, A], Unit])(implicit M: Monad[F]): Flow[F, A] =
      self
        .transform { value =>
          pf.applyOrElse[Either[CustomError, A], Unit](value, identity[Either[CustomError, A]])
          value
        }

  }

}
