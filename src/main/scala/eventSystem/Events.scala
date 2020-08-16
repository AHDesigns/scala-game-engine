package eventSystem

abstract class Event(val eventId: Int)

object GameLoopStart extends WithId
final case class GameLoopStart() extends Event(GameLoopStart.id.id)

object WindowResize extends WithId
final case class WindowResize(width: Int, height: Int) extends Event(WindowResize.id.id)

object Click extends WithId
final case class Click(x: Int, y: Int, keyCode: Int) extends Event(Click.id.id)

object SimpleEvent extends WithId
final case class SimpleEvent(data: AnyVal) extends Event(SimpleEvent.id.id)

// --------------- Helpers -------------------
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

