//package systems
//
//import ecs.{ComponentId, Delete, PlayerMovement, System, SystemMessage}
//import identifier.ID
//import org.joml.Vector3f
//import systems.physics.{Impulse, RigidBodySystem}
//import utils.Directions
//
//import scala.collection.mutable
//
//sealed trait PlayerMovementSystemMessage extends SystemMessage
//final case class PlayerMoveBy(x: Float, y: Float, z: Float, local: Boolean = true)
//    extends PlayerMovementSystemMessage
//final case class PlayerMoveTo(x: Float, y: Float, z: Float, local: Boolean = true)
//    extends PlayerMovementSystemMessage
//final case class PlayerTurnBy(x: Float, y: Float, z: Float) extends PlayerMovementSystemMessage
//final case class PlayerJump() extends PlayerMovementSystemMessage
//
//object PlayerMovementSystem extends System {
//  private val inverse = false
//  private val speed = 0.1f
//  private val rotSpeed = 0.00001f
//  private var movement = new Vector3f()
//  private var isMoving = false
//
//  private var msgQ = List.empty[PlayerMovementSystemMessage]
//  // TODO: is this a hack?
//  // is this ok for systems with few components like this one, or does this highlight
//  // how much of a faff getting this info is
//  private val activeEntities = mutable.HashMap.empty[ID, PlayerMovement]
//
//  def !(m: PlayerMovementSystemMessage): Unit = { msgQ = m :: msgQ }
//
//  def init(): Unit = {
//    components.on[PlayerMovement] { c => activeEntities += (c.entityId -> c) }
//    components.on[Delete](delIs(ComponentId.playerMovement) { c =>
//      activeEntities.remove(c.entityId)
//    })
//  }
//
//  private def handleMove(xyz: (Float, Float, Float)): Unit =
//    xyz match {
//      case (0, 0, 0) => isMoving = false
//      case (x, y, _) =>
//        movement = new Vector3f(x, 0, y).normalize().mul(speed)
//        isMoving = true
//    }
//
//  def update(time: Float): Unit = {
//    msgQ foreach {
//      case PlayerJump() =>
//        activeEntities foreach {
//          case (entity, _) =>
//            RigidBodySystem ! Impulse(entity, Directions.Up.toVec.mul(100))
//        }
//      case PlayerMoveTo(x, y, z, _) => ???
//      case PlayerMoveBy(x, y, z, _) => handleMove((x, y, z));
//      case PlayerTurnBy(x, y, z) =>
//        activeEntities foreach {
//          case (entity, component) =>
//            val newY = y * rotSpeed * (if (component.isCamera) -1 else 1)
//            val newX = x * rotSpeed * (if (inverse) -1 else 1)
//            MoveSystem ! Rotate(entity, newX, newY, 0)
//        }
//    }
//
//    msgQ = Nil
//
//    if (isMoving) {
//      activeEntities foreach {
//        case (entity, _) =>
//          MoveSystem ! Move(entity, movement, local = true)
//      }
//    }
//  }
//}
