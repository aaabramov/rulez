package controllers

import com.aaabramov.encoded.core.util.error.CustomError
import com.aaabramov.encoded.core.util.flow.FlowF
import play.api.Logging
import play.api.http.Writeable
import play.api.mvc.{BaseController, Result}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait ErrorHandling { this: BaseController with Logging =>

  type C
  implicit def writeable: Writeable[C]

  protected implicit def flow2result(flow: FlowF[Result])
                                    (implicit ec: ExecutionContext): Future[Result] =
    flow
      .value
      .transform {
        case Success(Right(result)) => Success(result)
        case Success(Left(error))   =>
          logger.error(s"Error occurred: $error")
          Success(Status(error.httpCode)(renderError(error)))
        case Failure(exception)     =>
          logger.error(s"Error occurred: $exception")
          Success(InternalServerError(renderEx(exception)))
      }

  def renderError(error: CustomError): C

  def renderEx(ex: Throwable): C


}
