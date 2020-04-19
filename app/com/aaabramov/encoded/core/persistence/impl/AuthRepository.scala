package com.aaabramov.encoded.core.persistence.impl

import com.aaabramov.encoded.core.controller.{AuthorizedUser, Credentials}
import com.aaabramov.encoded.core.entity.{Role, User}
import com.aaabramov.encoded.core.util.flow.FlowF
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[AuthRepositoryImpl])
trait AuthRepository {

  def auth(credentials: Credentials): FlowF[AuthorizedUser]

  def findDefault(): FlowF[Role]

  def findUsersWithRole(id: Int): FlowF[(Role, List[User])]

  def findUsersWithPermission(id: Int): FlowF[List[(User, Role)]]

}
