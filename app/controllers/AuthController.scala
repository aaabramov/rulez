package controllers

import java.util.UUID

import cats.instances.future._
import com.aaabramov.encoded.core.controller.Credentials
import com.aaabramov.encoded.core.entity.User
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.UsersRepository
import com.aaabramov.encoded.core.util.flow.Flow
import com.github.t3hnar.bcrypt._
import javax.inject.Inject
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

class AuthController @Inject()(
                                repo: UsersRepository,
                                now: Now
                              )
                              (implicit ec: ExecutionContext)
  extends RestController {

  def signUp() = Action.async(parse.json[Credentials]) { implicit req =>
    val body = req.body
    repo
      .list(Some(UserFilter(email = Some(body.email))))
      .flatMap {
        case Nil =>
          repo
            .insert(User(UUID.randomUUID(), body.email, body.password.bcrypt, 1, now(), now()))
            .map { user =>
              Json.obj("ok" -> true, "id" -> user.id)
            }

        case _ =>
          Flow.pure(
            Json.obj("ok" -> false, "error" -> "User with such email exists")
          )

      }
      .map(Ok(_))
  }

}
