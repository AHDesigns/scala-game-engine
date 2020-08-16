package object eventSystem {
  type ID = Int

  trait Events {
    val events: EventSystem.EventListener = EventSystem.subscribe()
  }
}
