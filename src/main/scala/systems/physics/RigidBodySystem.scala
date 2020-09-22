package systems.physics

import ecs.{ECS, RigidBody, System}
import eventSystem.{ComponentCreatedRigidBody, EventListener}
import identifier.ID
import org.joml.Vector3f
import systems.{Move, MoveSystem}
import utils.Directions

import scala.collection.mutable

object RigidBodySystem extends System with EventListener {
  final val activeEntities = mutable.HashSet.empty[ID]

  override def init(): Unit = {

    events.on[ComponentCreatedRigidBody] {
      case ComponentCreatedRigidBody(_, entity) => activeEntities += entity.id
    }
  }

  def update(timeElapsed: Double): Unit = {
    val entitiesWithRigidBody = ECS.demandComponents[RigidBody]
    entitiesWithRigidBody foreach {
      case (id, rb :: Nil) =>
        val res = calculateResultantDirection(rb, timeElapsed.toFloat, Nil)
        if (!res.equals(Directions.None.toVec)) {
          val deltaVec = res.mul(timeElapsed.toFloat, new Vector3f())
          MoveSystem ! Move(id, deltaVec, local = false)
        }
      case _ => ;
    }
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
