package controllers

import play.api.Logging

trait RestController
  extends JsonController
  with Logging
  with JsonErrorHandling
