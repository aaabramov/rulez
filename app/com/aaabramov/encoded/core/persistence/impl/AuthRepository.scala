package com.aaabramov.encoded.core.persistence.impl

import com.aaabramov.encoded.core.controller.{AuthorizedUser, Credentials}
import com.aaabramov.encoded.core.util.flow.FlowF
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[AuthRepositoryImpl])
trait AuthRepository {

  def auth(credentials: Credentials): FlowF[AuthorizedUser]

}
