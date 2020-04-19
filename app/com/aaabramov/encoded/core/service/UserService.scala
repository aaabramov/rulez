package com.aaabramov.encoded.core.service

import com.aaabramov.encoded.core.controller.{AuthorizedUser, Credentials}
import com.aaabramov.encoded.core.service.impl.UserServiceImpl
import com.aaabramov.encoded.core.util.flow.FlowF
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[UserServiceImpl])
trait UserService {

  def register(credentials: Credentials): FlowF[AuthorizedUser]

}
