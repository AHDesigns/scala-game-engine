package eventSystem

import observer.ObserverSystem

object EventSystem extends ObserverSystem[Event, EventId]

trait EventListener {
  val events: EventSystem.Observer = EventSystem.subscribe()
}
