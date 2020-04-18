package com.aaabramov.encoded.core.controller

import com.aaabramov.encoded.core.controller.dto.DeleteResponse
import controllers.RestController
import play.api.mvc.{Action, AnyContent}

trait CrudController[Id, Create, Update, Entity] { this: RestController =>

  def create(): Action[Create]

  def update(id: Id): Action[Update]

  def delete(id: Id): Action[AnyContent]

  def findOne(id: Id): Action[AnyContent]

  def list(): Action[AnyContent]

}
