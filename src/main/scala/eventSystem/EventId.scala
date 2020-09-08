package eventSystem

object EventId {
  implicit val gameLoopStart: EventId[GameLoopStart] = new EventId
  implicit val gameLoopTick: EventId[GameLoopTick] = new EventId
  implicit val gameLoopEnd: EventId[GameLoopEnd] = new EventId
  implicit val gameEnd: EventId[GameEnd] = new EventId
  implicit val windowClose: EventId[WindowClose] = new EventId
  implicit val windowResize: EventId[WindowResize] = new EventId
  implicit val click: EventId[Click] = new EventId
  implicit val simpleEvent: EventId[SimpleEvent] = new EventId()
  implicit val debugWireframe: EventId[DebugWireframe] = new EventId()
  implicit val inputMove: EventId[InputMove] = new EventId()
  implicit val mouseMove: EventId[MouseMove] = new EventId()
  implicit val cameraMove: EventId[CameraMove] = new EventId()
}

class EventId[E] extends Identifier
