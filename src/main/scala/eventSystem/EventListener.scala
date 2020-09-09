package eventSystem

trait EventListener {
  val events: EventSystem.EventListener = EventSystem.subscribe()
}
