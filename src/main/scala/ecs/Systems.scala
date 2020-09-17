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
    println("move system created")
    events.on[ComponentTransformCreated] {
      case ComponentTransformCreated(_, entity) =>
        println("adding")
        activeEntities += entity.id
    }

  }

  def update(): Unit = {
    println("processing move events")
    // process and empty msgs
    for (MoveEvent(id, x, y) <- msgs) {
      println(id, x, y)
      if (activeEntities.contains(id)) {
        println("in active list")
        Transform.data.get(id).map {
          case Transform(xn, yn) => Transform.data.update(id, Transform(x + xn, y + yn))
        }
      }
    }
    msgs = List.empty
  }
}
