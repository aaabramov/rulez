package com.aaabramov.encoded.core

import com.aaabramov.encoded.core.util.error.{CustomError, ExceptionError}
import com.aaabramov.encoded.core.util.flow.{Flow, FlowF}

import scala.concurrent.{ExecutionContext, Future}

/**
 * @author Andrii Abramov on 05.04.2020.
 */
package object persistence {

  def liftAction[A](f: Future[A])(implicit ec: ExecutionContext): FlowF[A] = Flow {
    f
      .map(Right(_))
      .recover {
        case ExceptionError(error) => Left(error)
        case ex                    => Left(CustomError.ex(ex))
      }

  }

}
