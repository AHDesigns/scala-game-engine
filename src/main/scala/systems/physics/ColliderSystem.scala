package systems.physics

import ecs.{System, SystemMessage}
import eventSystem.EventListener
import identifier.ID

sealed trait ColliderSystemMessage extends SystemMessage {
  val entityId: ID
}

object ColliderSystem extends System with EventListener {
  // TODO: add collider to handle impacts and send messages to RigidBodySystem
  override def init(): Unit = ???

  override def update(timeElapsed: Float): Unit = ???

}
