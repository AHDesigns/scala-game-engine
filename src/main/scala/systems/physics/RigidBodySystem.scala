package systems.physics

import ecs.{ECS, RigidBody, System, SystemMessage}
import eventSystem.{ComponentCreatedRigidBody, EventListener}
import identifier.ID
import org.joml.Vector3f
import systems.{Move, MoveSystem}
import utils.Directions

import scala.collection.mutable

sealed trait RigidBodySystemMessage extends SystemMessage {
  val entityId: ID
}
case class Impulse(entityId: ID, vector: Vector3f) extends RigidBodySystemMessage
case class Collision(entityId: ID, entity2Id: ID) extends RigidBodySystemMessage

//@deprecated("not deprecated, but more a shit WIP")
object RigidBodySystem extends System with EventListener {
  private var msgQ = Map.empty[ID, Seq[RigidBodySystemMessage]]

  final val activeEntities = mutable.HashSet.empty[ID]

  override def init(): Unit = {
    events.on[ComponentCreatedRigidBody] {
      case ComponentCreatedRigidBody(_, entity) => activeEntities += entity.identifier
    }
  }

  def !(m: RigidBodySystemMessage): Unit = {
    val existingMs = msgQ.getOrElse(m.entityId, Seq.empty)
    msgQ = msgQ + (m.entityId -> (m +: existingMs))
  }

  def update(timeElapsed: Float): Unit = {
    val entitiesWithRigidBody = ECS.demandComponents[RigidBody]

    entitiesWithRigidBody foreach {
      case (id, rb :: Nil) =>
        val impulses = msgQ.getOrElse(id, Seq.empty) map {
          case Impulse(_, vector) => vector
          case _: Collision       => rb.vFinal.set(0)
        }
        val res = calculateResultantDirection(rb, timeElapsed.toFloat, impulses)
        if (!res.equals(Directions.None.toVec)) {
          val deltaVec = res.mul(timeElapsed, new Vector3f())
          MoveSystem ! Move(id, deltaVec, local = false)
          ColliderSystem ! CheckCollisions(id)
        }
      case _ => ;
    }
    msgQ = Map.empty
  }

  private def calculateResultantDirection(
      rb: RigidBody,
      timeElapsed: Float,
      forces: Seq[Vector3f]
  ): Vector3f = {
    val allForces = if (rb.gravity) Earth.gravity +: forces else forces
    val acceleration = (Directions.None.toVec +: allForces).reduce(_.add(_))
    rb.vFinal.add(acceleration.mul(timeElapsed))
  }
}

object Earth {
  val gravity: Vector3f = Directions.Down.toVec.mul(9.8f)
}
