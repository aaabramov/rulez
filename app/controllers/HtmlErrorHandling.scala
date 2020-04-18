package controllers

import com.aaabramov.encoded.core.util.error.CustomError
import play.api.Logging
import play.api.http.Writeable
import play.api.mvc.BaseController
import play.twirl.api.Html
import views.{html => Render}

trait HtmlErrorHandling extends ErrorHandling { this: BaseController with Logging =>

  override type C = Html

  override implicit def writeable: Writeable[Html] = Writeable.writeableOf_Content

  override def renderEx(ex: Throwable): Html =
    Render.index("Error", s"Internal error")

  override def renderError(error: CustomError): Html =
    Render.index("Error", s"Error: ${error.apiMessage}")
}
