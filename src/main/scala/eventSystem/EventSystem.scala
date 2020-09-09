package eventSystem

import identifier._

import scala.collection.immutable.Queue

object EventSystem {
  private var lastId = 1
  private var listeners: Map[IdWrapper, Queue[(ID, _ >: Event => Unit)]] =
    Map.empty

  def subscribe(): EventListener = {
    lastId += 1
    new EventListener(lastId)
  }

  def ![E <: Event](event: E)(implicit e: EventId[E]): Unit = {
    for (
      cbs <- listeners.get(e.id);
      (_, cb) <- cbs
    ) cb.asInstanceOf[E => Unit](event)
  }

  class EventListener(private val listenerId: ID) {
    private var listening = true

    def on[E <: Event](cb: E => Unit)(implicit e: EventId[E]): EventListener = {
      if (!listening) {
        println(
          s"Listener is no longer subscribed to events. Attempted to listen to $e"
        )
      } else {
        listeners += (e.id -> listeners
          .getOrElse(e.id, Queue.empty)
          .enqueue(listenerId, cb))
      }
      this
    }

    def unsubscribe(): Unit = {
      if (listening) {
        listeners = listeners.map {
          case (k, v) =>
            (k, v.filter { case (id, _) => id != listenerId })
        }
        listening = false
      }
    }
  }
}
