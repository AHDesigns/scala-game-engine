package systems.physics

import ecs.colliders.{Cube, Plane, Quad, Sphere}
import ecs.{Collider, Entity, System, SystemMessage, Transform}
import eventSystem.{ComponentCreatedCollider, ComponentCreatedTransform, EventListener}
import identifier.ID
import org.joml.Vector3f
import utils.Axis

import scala.collection.immutable.Queue

// TODO: make much better with https://www.toptal.com/game/video-game-physics-part-ii-collision-detection-for-solid-objects

sealed trait ColliderSystemMessage extends SystemMessage {
  val entityId: ID
}
case class CheckCollisions(entityId: ID) extends ColliderSystemMessage

case class ColliderPosition(collider: Collider, transform: Transform)
object ColliderSystem extends System with EventListener {
  private var msgQ = Queue.empty[ColliderSystemMessage]

  var collidables = Map.empty[ID, ColliderPosition]

  def !(m: ColliderSystemMessage): Unit = {
    msgQ = msgQ.appended(m)
  }

  override def init(): Unit = {
    events
      .on[ComponentCreatedCollider] {
        case ComponentCreatedCollider(_, entity) => storeComponent(entity)
      }
      .on[ComponentCreatedTransform] {
        case ComponentCreatedTransform(_, entity) => storeComponent(entity)
      }
  }

  private def storeComponent(entity: Entity): Unit = {
    (entity.getComponent[Transform], entity.getComponent[Collider]) match {
      case (Some(transform :: Nil), Some(collider :: Nil)) =>
        collidables = collidables + (entity.id -> ColliderPosition(collider, transform))
      case _ => ;
    }
  }

  private def calculateCollision(
      entityId: ID,
      axis: Axis.Val,
      position: Float,
      collidables: Map[ID, ColliderPosition]
  ): Unit = {
    collidables.foreach {
      case (id, ColliderPosition(Collider(Plane(axis, offset)), Transform(position, _, _))) =>
        val targetPosition = axisOffset(axis, position, offset)
        axis match {
          case Axis.X => if (position.x <= targetPosition) println("collision x")
          case Axis.Y => if (position.y <= targetPosition) RigidBodySystem ! Collision(entityId, id)
          case Axis.Z => if (position.z <= targetPosition) println("collision z")
          case _      =>
        }
    }
  }

  private def axisOffset(axis: Axis.Val, position: Vector3f, offset: Float) =
    axis match {
      case Axis.X => position.x + offset
      case Axis.Y => position.y + offset
      case Axis.Z => position.z + offset
    }

  override def update(timeElapsed: Float): Unit = {
    msgQ.filter(msg => collidables.contains(msg.entityId)) foreach {
      case CheckCollisions(entityId) => {
        val ColliderPosition(Collider(shape), Transform(targetPosition, _, _)) =
          collidables.getOrElse(entityId, throw new RuntimeException("could not find collidabel"))
        shape match {
          case Plane(axis, offset) =>
            val position = axisOffset(axis, targetPosition, offset)
            calculateCollision(entityId, axis, position, collidables)
          case Quad(x, y)    => ;
          case Sphere(r)     => ;
          case Cube(x, y, z) => ;
        }
      }
    }
  }
}
