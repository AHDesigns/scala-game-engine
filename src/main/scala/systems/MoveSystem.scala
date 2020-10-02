package systems

import ecs.{ComponentId, Delete, ECS, System, SystemMessage, Transform}
import identifier.ID
import org.joml.Vector3f
import utils.Direction

import scala.collection.immutable.Queue
import scala.collection.mutable

sealed trait MoveSystemMessage extends SystemMessage {
  val entityId: ID
}
case class Move(entityId: ID, vector: Vector3f, local: Boolean) extends MoveSystemMessage
object Move {
  def apply(entityId: ID, direction: Direction, local: Boolean = true) =
    new Move(entityId, direction.toVec, local)
}

case class Rotate(entityId: ID, x: Float, y: Float, z: Float) extends MoveSystemMessage

object MoveSystem extends System {
  private var msgQ = Queue.empty[MoveSystemMessage]

  final val activeEntities = mutable.HashSet.empty[ID]

  def !(m: MoveSystemMessage): Unit = { msgQ = msgQ.appended(m) }

  def init(): Unit = {
    components
      .on[Transform] { activeEntities += _.entityId }
      .on[Delete](delIs(ComponentId.transform) { activeEntities -= _.entityId })
  }

  def update(time: Float): Unit = {
    val entitiesWithTransforms = ECS.demandComponents[Transform]
    msgQ.filter(msg => activeEntities.contains(msg.entityId)) foreach {
      case Rotate(id, x, y, z) =>
        val currentTransforms = entitiesWithTransforms.getOrElse(id, throw Errors.noEntity)
        entitiesWithTransforms.update(
          id,
          currentTransforms.map { t: Transform =>
            t.rotation.y += y
            t.rotation.x += x
            t
          }
        )
      case Move(id, vector, local) =>
        val currentTransforms = entitiesWithTransforms.getOrElse(id, throw Errors.noEntity)
        entitiesWithTransforms.update(
          id,
          currentTransforms.map { t: Transform =>
            val newDirection = {
              if (local) {
                vector.rotateY(t.rotation.y, new Vector3f())
              } else {
                vector
              }
            }
            t.position.add(newDirection)
            t
          }
        )
    }
    msgQ = Queue.empty
  }
}

object Errors {
  val noEntity = new RuntimeException(
    "unable to find active entity from transforms, maybe it's transform was removed without an event?"
  )
}
