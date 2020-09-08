package behaviours

import entities.Entity

// TODO: put event system into init and update, probably
trait Behaviour {
  def init(entity: Entity): Unit

  def update(): Unit

  var me: Entity = Entity.empty
}
