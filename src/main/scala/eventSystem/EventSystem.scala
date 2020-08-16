package eventSystem

object EventSystem {
  private var lastId = 1
  private var listeners = Map.empty[ID, List[(ID, Event => Unit)]]

  def subscribe(): EventListener = {
    lastId += 1
    new EventListener(lastId)
  }

  def !(e: Event): Unit = {
    for (cbs <- listeners.get(e.eventId);
         (_, cb) <- cbs) cb(e)
  }

  class EventListener(private val listenerId: ID) {
    private var listening = true

    def on(e: IdWrapper, cb: Event => Unit): EventListener = {
      if (!listening) {
        println(s"Listener is no longer subscribed to events. Attempted to listen to $e")
      } else {
        listeners += (e.id -> ((listenerId, cb) :: listeners.getOrElse(e.id, Nil)))
      }
      this
    }

    def unsubscribe(): Unit = {
      if (listening) {
        listeners = listeners.map { case (k, v) =>
          (k, v.filter { case (id, _) => id != listenerId })
        }
        listening = false
      }
    }
  }
}