package eventSystem

import ecs._
import identifier.Identifier
import org.joml.Matrix4f

object EventId {
  // TODO could a macro save some ball ache?
  implicit val gameLoopStart: EventId[GameLoopStart] = new EventId
  implicit val gameLoopTick: EventId[GameLoopTick] = new EventId
  implicit val gameLoopEnd: EventId[GameLoopEnd] = new EventId
  implicit val gameEnd: EventId[GameEnd] = new EventId
  implicit val windowClose: EventId[WindowClose] = new EventId
  implicit val windowResize: EventId[WindowResize] = new EventId
  implicit val click: EventId[Click] = new EventId
  implicit val simpleEvent: EventId[SimpleEvent] = new EventId
  implicit val emptyEvent: EventId[EmptyEvent] = new EventId
  implicit val debugWireframe: EventId[DebugWireframe] = new EventId
  implicit val inputMove: EventId[InputMove] = new EventId
  implicit val mouseMove: EventId[MouseMove] = new EventId
  implicit val cameraMove: EventId[CameraMove] = new EventId
  implicit val entityCreated: EventId[EntityCreated] = new EventId
  implicit val entityDeleted: EventId[EntityDestroyed] = new EventId

  implicit val componentTransformCreated: EventId[ComponentCreatedTransform] = new EventId
  implicit val componentModelCreated: EventId[ComponentCreatedModel] = new EventId
  implicit val componentCameraCreated: EventId[ComponentCreatedCamera] = new EventId
  implicit val componentLightCreated: EventId[ComponentCreatedLight] = new EventId
  implicit val componentPlayerMovementCreated: EventId[ComponentCreatedPlayerMovement] = new EventId
  implicit val componentCreatedCollider: EventId[ComponentCreatedCollider] = new EventId
  implicit val componentCreatedRigidBody: EventId[ComponentCreatedRigidBody] = new EventId
}

class EventId[E] extends Identifier

sealed trait Event

// TODO: make all these pass a data val and use in callback to aid extraction
// Game Events -------------------------------------------------------
final case class GameLoopStart() extends Event
final case class GameLoopTick(timeElapsed: Float) extends Event
final case class GameLoopEnd() extends Event
final case class GameEnd() extends Event

// Window Events -----------------------------------------------------
final case class WindowClose() extends Event
final case class WindowResize(width: Int, height: Int) extends Event

// Inputs ------------------------------------------------------------
final case class Click(x: Int, y: Int, keyCode: Int) extends Event
final case class MouseMove(x: Int, y: Int) extends Event
final case class InputMove(x: Float, y: Float, z: Float) extends Event

// Camera ------------------------------------------------------------
final case class CameraMove(transform: Matrix4f) extends Event

// Entity events ------------------------------------------------------------
final case class EntityCreated(entity: Entity) extends Event
final case class EntityDestroyed(entity: Entity) extends Event

// Component events --------------------------------------------------
final case class ComponentCreatedTransform(component: Transform, entity: Entity) extends Event
final case class ComponentCreatedModel(component: Model, entity: Entity) extends Event
final case class ComponentCreatedCamera(component: Camera, entity: Entity) extends Event
final case class ComponentCreatedLight(component: Light, entity: Entity) extends Event
final case class ComponentCreatedPlayerMovement(component: PlayerMovement, entity: Entity)
    extends Event
final case class ComponentCreatedCollider(component: Collider, entity: Entity) extends Event
final case class ComponentCreatedRigidBody(component: RigidBody, entity: Entity) extends Event

// Debug/Random Events -----------------------------------------------
final case class EmptyEvent() extends Event
final case class SimpleEvent(data: AnyVal) extends Event
final case class DebugWireframe() extends Event
