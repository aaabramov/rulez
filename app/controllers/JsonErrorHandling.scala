package controllers

import com.aaabramov.encoded.core.util.error.CustomError
import play.api.Logging
import play.api.http.Writeable
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.BaseController

trait JsonErrorHandling extends ErrorHandling { this: BaseController with Logging =>

  override type C = JsValue
  override implicit def writeable: Writeable[JsValue] = Writeable.writeableOf_JsValue

  override def renderEx(ex: Throwable): JsValue =
    Json.obj(
      "ok" -> false,
      "error" -> "Internal error"
    )

  override def renderError(error: CustomError): JsValue =
    Json.obj(
      "ok" -> false,
      "error" -> error.apiMessage
    )
}
