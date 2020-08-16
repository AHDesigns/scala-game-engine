package eventSystem

/** Base Event
 *
 * the eventId allows for matching all listeners of an event enum. to their respective callback
 */
abstract class Event(val eventId: Int)
object GameLoopStartEvent { val eventId: ID = EventID.gen() }
final case class GameLoopStartEvent() extends Event(GameLoopStartEvent.eventId)

object WindowResizeEvent { val eventId: ID = EventID.gen() }
final case class WindowResizeEvent(width: Int, height: Int) extends Event(WindowResizeEvent.eventId)

object ClickEvent { val eventId: ID = EventID.gen() }
final case class ClickEvent(x: Int, y: Int, keyCode: Int) extends Event(ClickEvent.eventId)

object SimpleEvent { val eventId: ID = EventID.gen() }
final case class SimpleEvent(data: AnyVal) extends Event(SimpleEvent.eventId)

object SomeNewEvent { val eventId: ID = EventID.gen() }
final case class SomeNewEvent(data: AnyVal) extends Event(SomeNewEvent.eventId)
