package eventSystem

object Events extends Enumeration {
  val
  gameLoopStart,
  windowResize, windowClose,
  click,
  simple
  = Value
}


/** Base Event
 *
 * the eventId allows for matching all listeners of an event enum. to their respective callback
 */
abstract class Event(val eventId: Events.Value)
final case class GameLoopStartEvent() extends Event(Events.gameLoopStart)
final case class WindowResizeEvent(width: Int, height: Int) extends Event(Events.windowResize)
final case class WindowCloseEvent() extends Event(Events.windowClose)
final case class ClickEvent(x: Int, y: Int, mouseKey: Int) extends Event(Events.click)
final case class SimpleEvent(data: AnyVal) extends Event(Events.simple)
