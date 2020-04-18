package controllers

import java.util.UUID

import cats.instances.future._
import com.aaabramov.encoded.core.entity.User
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.UsersRepository
import com.aaabramov.encoded.core.util.flow.Flow
import com.github.t3hnar.bcrypt._
import javax.inject.Inject
import play.api.libs.json.{Json, OFormat}

import scala.concurrent.ExecutionContext

case class Register(email: String, password: String)

object Register {
  implicit val format: OFormat[Register] = Json.format[Register]
}

class AuthController @Inject()(
                                repo: UsersRepository,
                                now: Now
                              )
                              (implicit ec: ExecutionContext)
  extends RestController {

  def signUp() = Action.async(parse.json[Register]) { implicit req =>
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

  def signIn() = Action.async(parse.json[Register]) { implicit req =>
    val body = req.body

    repo
      .list(Some(UserFilter(email = Some(body.email))))
      .map {
        case Nil        => Json.obj("ok" -> false, "error" -> "User not found")
        case List(user) =>
          if (body.password.isBcrypted(user.password)) {
            Json.obj("ok" -> true)
          } else {
            Json.obj("ok" -> false, "error" -> "Wrong password")
          }
      }
      .map(Ok(_))
  }

}
