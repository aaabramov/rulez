package com.aaabramov.encoded.core.service.impl

import java.util.UUID

import cats.instances.future._
import com.aaabramov.encoded.core.controller.{AuthorizedUser, Credentials}
import com.aaabramov.encoded.core.entity.{Role, User}
import com.aaabramov.encoded.core.module.Now
import com.aaabramov.encoded.core.persistence.UsersRepository
import com.aaabramov.encoded.core.persistence.impl.AuthRepository
import com.aaabramov.encoded.core.service.UserService
import com.aaabramov.encoded.core.util.error.CustomError
import com.aaabramov.encoded.core.util.flow.{Flow, FlowF}
import com.github.t3hnar.bcrypt._
import controllers.UserFilter
import javax.inject.Inject
import play.api.http.Status

import scala.concurrent.ExecutionContext

class UserServiceImpl @Inject()(
                                 userRepo: UsersRepository,
                                 authRepo: AuthRepository,
                                 now: Now
                               )
                               (implicit ec: ExecutionContext)
  extends UserService {

  override def register(credentials: Credentials): FlowF[AuthorizedUser] =
    userRepo
      .list(Some(UserFilter(email = Some(credentials.email))))
      .flatMap {
        case Nil =>
          authRepo
            .findDefault()
            .flatMap { role =>
              val time = now()
              userRepo.insert(User(
                UUID.randomUUID(),
                credentials.email.toLowerCase,
                credentials.password.bcrypt,
                role.id,
                time,
                time
              ))
                .flatMap { _ =>
                  authRepo.auth(credentials)
                }
            }
        case _   =>
          Flow.leftT(CustomError.free("User with such email already exists", s"User with email=${credentials.email} already exists", Status.CONFLICT))
      }

}
