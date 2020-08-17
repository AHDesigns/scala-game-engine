package eventSystem

abstract class Event(val eventId: Int)

// Game Events -------------------------------------------------------
object GameLoopStart extends WithId
final case class GameLoopStart() extends Event(GameLoopStart.id.id)

object GameLoopTick extends WithId
final case class GameLoopTick() extends Event(GameLoopTick.id.id)

object GameLoopEnd extends WithId
final case class GameLoopEnd() extends Event(GameLoopEnd.id.id)

object GameEnd extends WithId
final case class GameEnd() extends Event(GameEnd.id.id)

// Window Events -----------------------------------------------------
object WindowClose extends WithId
final case class WindowClose() extends Event(WindowClose.id.id)

object WindowResize extends WithId
final case class WindowResize(width: Int, height: Int) extends Event(WindowResize.id.id)

// Inputs ------------------------------------------------------------
object Click extends WithId
final case class Click(x: Int, y: Int, keyCode: Int) extends Event(Click.id.id)

// Debug/Random Events -----------------------------------------------
object SimpleEvent extends WithId
final case class SimpleEvent(data: AnyVal) extends Event(SimpleEvent.id.id)

// --------------- Helpers -------------------------------------------
private object EventID {
  private var lastId: ID = 0;

  def gen(): EventIDWrapper = {
    lastId += 1
    new EventIDWrapper(lastId)
  }
}

class EventIDWrapper(val id: ID)

trait WithId {
  val id: EventIDWrapper = EventID.gen()
}

