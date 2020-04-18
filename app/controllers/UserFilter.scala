package controllers

import java.util.UUID

case class UserFilter(
                       uuid: Option[UUID] = None,
                       email: Option[String] = None
                     )
