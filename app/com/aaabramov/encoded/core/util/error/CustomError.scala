package com.aaabramov.encoded.core.util.error

import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.libs.json.{Json, OFormat}

sealed trait CustomError {
  def apiMessage: String

  def internalMessage: String

  def httpCode: Int

  def exception: Option[Throwable]
}

object CustomError {

  private case class FreeError(
                                apiMessage: String,
                                internalMessage: String,
                                httpCode: Int) extends CustomError {
    override def exception: Option[Throwable] = None

    override def toString: String = this.asJsonString
  }

  private object FreeError {
    implicit val format: OFormat[FreeError] = Json.format[FreeError]
  }

  def free(apiMessage: String, internalMessage: String, httpCode: Int): CustomError =
    FreeError(apiMessage, internalMessage, httpCode)

  def entityNotFound(entity: String, id: String): CustomError =
    InternalError(
      "Not found",
      s"Entity $entity with id = $id was not found",
      404
    )

  def ex(ex: Throwable): CustomError =
    InternalError(
      "Exception occurred",
      ex.getMessage,
      500
    )

}

case class InternalError(
                          apiMessage: String,
                          internalMessage: String,
                          httpCode: Int,
                          exception: Option[Throwable] = None
                        ) extends CustomError

case class ExceptionError(error: CustomError) extends Throwable with CustomError {
  override val apiMessage: String = error.apiMessage

  override val internalMessage: String = error.internalMessage

  override val httpCode: Int = error.httpCode

  override val exception: Option[Throwable] = error.exception

  override val getMessage: String = internalMessage
}