package com.aaabramov.encoded.core.controller

import java.time.Clock

import cats.instances.future._
import com.aaabramov.encoded.core.persistence.impl.AuthRepository
import com.aaabramov.encoded.core.service.UserService
import com.aaabramov.encoded.core.util.json.AnyAsJson
import controllers.{JsonController, JsonErrorHandling, RestController}
import javax.inject.Inject
import pdi.jwt.JwtSession
import play.api.{Configuration, Logging}

import scala.concurrent.ExecutionContext

class AuthController @Inject()(
                                userService: UserService,
                                authRepository: AuthRepository
                              )(implicit ec: ExecutionContext, conf: Configuration, clock: Clock)
  extends RestController {

  def auth = Action.async(parse.json[Credentials]) { req =>
    val credentials = req.body

    authRepository
      .auth(credentials)
      .map { user =>
        Ok(
          AuthResponse(ok = true, jwt = Some(JwtSession(user.asJsonObject).serialize))
        )
      }

  }

  def signUp() = Action.async(parse.json[Credentials]) { implicit req =>
    userService
      .register(req.body)
      .map { user =>
        AuthResponse(ok = true, jwt = Some(JwtSession(user.asJsonObject).serialize))
      }
      .map(Ok(_))
      .recover { case error =>
        Status(error.httpCode)(AuthResponse(ok = false, error = Some(error.apiMessage)))
      }
  }

}
