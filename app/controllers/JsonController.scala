package controllers

import akka.util.ByteString
import com.aaabramov.encoded.core.util.json.AnyAsJson
import play.api.http.{ContentTypes, Writeable}
import play.api.libs.json.{JsValue, Writes}
import play.api.mvc.{Codec, InjectedController}

/**
 * @author Andrii Abramov on 05.04.2020.
 */
trait JsonController extends InjectedController {

  private val json2bytes: JsValue => ByteString = { json =>
    Codec.utf_8.encode(json.toString)
  }

  /**
   * Makes a Writable[A] typeclass for any type of A with Writes[A] in scope
   */
  protected implicit def writableJson[A: Writes]: Writeable[A] = {
    val transform = (any: A) => json2bytes(any.asJson)
    Writeable[A](transform, Some(ContentTypes.JSON))
  }

}
