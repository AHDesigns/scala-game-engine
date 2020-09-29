package observer

import identifier.ID.IdValue
import identifier.{ID, Identifier}

import scala.collection.immutable.Queue

class ObserverSystem[Observable, ObservableId[_] <: Identifier] {
  private var lastId = 1
  private var listeners: Map[ID, Queue[(IdValue, _ >: Observable => Unit)]] =
    Map.empty

  def subscribe(): Observer = {
    lastId += 1
    new Observer(lastId)
  }

  def ![E <: Observable](event: E)(implicit e: ObservableId[E]): Unit = {
    for (
      cbs <- listeners.get(e.id);
      (_, cb) <- cbs
    ) cb.asInstanceOf[E => Unit](event)
  }

  // TODO: support un-listening to a single event
  class Observer(private val listenerId: IdValue) {
    private var listening = true

    def on[E <: Observable](cb: E => Unit)(implicit e: ObservableId[E]): Observer = {
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
