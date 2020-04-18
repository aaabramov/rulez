package com.aaabramov.encoded.core.util.error

sealed trait CustomError {
  def apiMessage: String

  def internalMessage: String

  def httpCode: Int

  def exception: Option[Throwable]
}

object CustomError {

  def free(_apiMessage: String, _internalMessage: String, _httpCode: Int): CustomError = new CustomError {
    override def apiMessage: String = _apiMessage

    override def internalMessage: String = _internalMessage

    override def httpCode: Int = _httpCode

    override def exception: Option[Throwable] = None
  }

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