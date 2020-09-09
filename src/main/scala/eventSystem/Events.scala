package eventSystem

import identifier.Identifier
import org.joml.Matrix4f

object EventId {
  implicit val gameLoopStart: EventId[GameLoopStart] = new EventId
  implicit val gameLoopTick: EventId[GameLoopTick] = new EventId
  implicit val gameLoopEnd: EventId[GameLoopEnd] = new EventId
  implicit val gameEnd: EventId[GameEnd] = new EventId
  implicit val windowClose: EventId[WindowClose] = new EventId
  implicit val windowResize: EventId[WindowResize] = new EventId
  implicit val click: EventId[Click] = new EventId
  implicit val simpleEvent: EventId[SimpleEvent] = new EventId
  implicit val debugWireframe: EventId[DebugWireframe] = new EventId
  implicit val inputMove: EventId[InputMove] = new EventId
  implicit val mouseMove: EventId[MouseMove] = new EventId
  implicit val cameraMove: EventId[CameraMove] = new EventId
}

class EventId[E] extends Identifier

sealed trait Event

// Game Events -------------------------------------------------------
final case class GameLoopStart() extends Event

final case class GameLoopTick() extends Event

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

// Debug/Random Events -----------------------------------------------
final case class SimpleEvent(data: AnyVal) extends Event

final case class DebugWireframe() extends Event
