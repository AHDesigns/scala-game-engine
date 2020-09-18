package ecs

import eventSystem.{ComponentTransformCreated, EventListener}
import identifier.{ID, Identifier}

import scala.collection.mutable

trait System extends Identifier {}

case class MoveEvent(entityId: ID, x: Int, y: Int)

object MoveSystem extends System with EventListener {
  var msgs = List.empty[MoveEvent]

  final val activeEntities = mutable.HashSet.empty[ID]

  def addMsg(m: MoveEvent): Unit = { msgs = m :: msgs }

  def init(): Unit = {
    events.on[ComponentTransformCreated] {
      case ComponentTransformCreated(_, entity) =>
        println("adding")
        activeEntities += entity.id
    }
  }

  def update(): Unit = {
    ECS.getComponents[Transform] foreach (entityComponents => {
      for (MoveEvent(id, x, y) <- msgs if activeEntities.contains(id)) {
        val currentTransforms = entityComponents.getOrElse(
          id,
          throw new RuntimeException("i think this should happen")
        )
        entityComponents.update(
          id,
          currentTransforms.map {
            case Transform(xn, yn) => Transform(x + xn, y + yn)
          }
        )
      }
      msgs = List.empty
    })
  }
}
