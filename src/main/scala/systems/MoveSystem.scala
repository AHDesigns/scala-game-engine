package systems

import components.{ECS, Transform}
import eventSystem.{ComponentTransformCreated, EventListener}
import identifier.ID

import scala.collection.mutable

sealed trait MoveSystemMessage extends SystemMessage
case class SimpleMove(entityId: ID, x: Float, y: Float) extends MoveSystemMessage
object MoveSystem extends System with EventListener {
  var msgs = List.empty[MoveSystemMessage]

  final val activeEntities = mutable.HashSet.empty[ID]

  def addMsg(m: SimpleMove): Unit = { msgs = m :: msgs }

  def init(): Unit = {
    events.on[ComponentTransformCreated] {
      case ComponentTransformCreated(_, entity) =>
        println("adding")
        activeEntities += entity.id
    }
  }

  def update(): Unit = {
    ECS.getComponents[Transform] foreach (entityComponents => {
      msgs.filter(msg => activeEntities.contains(msg.entityId)) foreach {
        case SimpleMove(id, x, y) =>
          val currentTransforms = entityComponents.getOrElse(
            id,
            throw new RuntimeException("i think this should happen")
          )
          entityComponents.update(
            id,
            currentTransforms.map { t: Transform =>
              t.position.add(x, y, 0f)
              t
            }
          )
      }
      msgs = List.empty
    })
  }
}
