package com.aaabramov.encoded.core.util

import play.api.libs.json._

package object json {

  implicit class AnyAsJson[A](val self: A) extends AnyVal {
    def asJson(implicit w: Writes[A]): JsValue =
      w.writes(self)

    def asJsonObject(implicit w: OWrites[A]): JsObject =
      w.writes(self)

    def asJsonString(implicit w: Writes[A]): String =
      Json.stringify(w.writes(self))

    def asJsonPretty(implicit w: Writes[A]): String =
      Json.prettyPrint(self.asJson)
  }

}
