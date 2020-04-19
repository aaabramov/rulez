package com.aaabramov.encoded.core.persistence

import com.aaabramov.encoded.core.controller.dto.DeleteResponse
import com.aaabramov.encoded.core.util.Mutation
import com.aaabramov.encoded.core.util.flow.FlowF

trait CrudRepository[Id, Entity] {

  def create(entity: Entity): FlowF[Entity]

  def update(id: Id)(mutation: Mutation[Entity]): FlowF[Entity]

  def delete(id: Id): FlowF[DeleteResponse]

  def findOne(id: Id): FlowF[Entity]

  def list(): FlowF[List[Entity]]

}
