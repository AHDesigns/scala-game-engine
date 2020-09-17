package components

import entities.Entity
import identifier.Identifier

// TODO: put event system into init and update, probably
trait Component extends Identifier {
  final var me: Entity = Entity.empty
  final var hasUpdate = true

  final def bind(entity: Entity): Unit = {
    me = entity
//    EventSystem ! ComponentCreated(this)
  }

  /**
    * Init is invoked on entity construction
    * it is intended to be used to register with a system
    */
  def init(): Unit = {}

  /** Update is invoked every game tick */
  def update(): Unit = {
    // prevent update calls if trait behavior is not overridden
    hasUpdate = false
  }

  /**
    * Destroy is invoked on Entity destruction
    * it is intended to be used to unregister from a system
    */
  def destroy(): Unit = {}
}
