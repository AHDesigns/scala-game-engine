package behaviours

import entities.{Entity, Transform}
import eventSystem.{EntityCreated, EventListener}
import identifier.ID
import org.joml.Vector3f

class Follow(entityId: ID, displacement: Transform) extends Behaviour with EventListener {
  var following: Option[Entity] = None

  override def init(): Unit = {
    Entity.entities.get(entityId) match {
      case Some(e) =>
        following = Some(e)
      case None =>
        events.on[EntityCreated] {
          case EntityCreated(e) =>
            if (e.id == entityId) {
              following = Some(e)
              events.unsubscribe()
            }
        }
    }
  }

  override def update(): Unit = {
    following.foreach {
      case Entity(Transform(position, rotation, scale), _, _, _, _) =>
        me.transform.position = position add (displacement.position, new Vector3f())
        me.transform.rotation = rotation + displacement.rotation
    }
  }
}
