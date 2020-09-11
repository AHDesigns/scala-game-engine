package behaviours

import entities.Entity

// TODO: put event system into init and update, probably
trait Behaviour {
  final var me: Entity = Entity.empty
  final var hasUpdate = true

  final def bind(entity: Entity): Unit = me = entity

  /** Init is invoked on entity construction */
  def init(): Unit

  /** Update is invoked every game tick */
  def update(): Unit = {
    // prevent update calls if trait behavior is not overridden
    hasUpdate = false
  }
}
