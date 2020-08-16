package eventSystem

private object EventID {
  private var lastId: ID = 0;

  def gen(): IdWrapper = {
    lastId += 1
    new IdWrapper(lastId)
  }
}

class IdWrapper(val id: ID)

trait WithId {
  val id: IdWrapper = EventID.gen()
}
/** Base Event
 *
 * the eventId allows for matching all listeners of an event enum. to their respective callback
 */
abstract class Event(val eventId: Int)

object GameLoopStartEvent extends WithId
final case class GameLoopStartEvent() extends Event(GameLoopStartEvent.id.id)

object WindowResizeEvent extends WithId
final case class WindowResizeEvent(width: Int, height: Int) extends Event(WindowResizeEvent.id.id)

object ClickEvent extends WithId
final case class ClickEvent(x: Int, y: Int, keyCode: Int) extends Event(ClickEvent.id.id)

object SimpleEvent extends WithId
final case class SimpleEvent(data: AnyVal) extends Event(SimpleEvent.id.id)
