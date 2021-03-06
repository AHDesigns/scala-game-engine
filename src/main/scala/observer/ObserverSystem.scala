package observer

import identifier.ID.IdValue
import identifier.{ID, Identifier}
import logging.Logger

import scala.collection.immutable.Queue

class ObserverSystem[Observable, ObservableId[_] <: Identifier] extends Logger {
  private var lastId = 1
  private var listeners: Map[ID, Queue[(IdValue, _ >: Observable => Unit)]] =
    Map.empty

  def subscribe(): Observer = {
    lastId += 1
    new Observer(lastId)
  }

  def ![E <: Observable](event: E)(implicit e: ObservableId[E]): Unit = {
    for (
      cbs <- listeners.get(e.identifier);
      (_, cb) <- cbs
    ) cb.asInstanceOf[E => Unit](event)
  }

  // TODO: support un-listening to a single event
  class Observer(private val listenerId: IdValue) {
    private var listening = true

    def on[E <: Observable](cb: E => Unit)(implicit e: ObservableId[E]): Observer = {
      if (!listening) {
        logWarn(
          s"Listener is no longer subscribed to events. Attempted to listen to $e"
        )
      } else {
        listeners += (e.identifier -> listeners
          .getOrElse(e.identifier, Queue.empty)
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
