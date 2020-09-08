package eventSystem

trait Events {
  val events: EventSystem.EventListener = EventSystem.subscribe()
}
