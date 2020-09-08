package eventSystem

import org.joml.Matrix4f

import scala.collection.immutable.Queue

object EventSystem {
  private var lastId = 1
  private var listeners: Map[IDWrapper, Queue[(ID, _ >: Event => Unit)]] =
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

    def on[E <: Event](
        cb: E => Unit
    )(implicit e: EventId[E]): EventListener = {
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

sealed trait Event
// Game Events -------------------------------------------------------
final case class GameLoopStart() extends Event
final case class GameLoopTick() extends Event

final case class GameLoopEnd() extends Event

final case class GameEnd() extends Event

// Window Events -----------------------------------------------------
final case class WindowClose() extends Event

final case class WindowResize(width: Int, height: Int) extends Event

// Inputs ------------------------------------------------------------
final case class Click(x: Int, y: Int, keyCode: Int) extends Event

final case class MouseMove(x: Int, y: Int) extends Event

final case class InputMove(x: Float, y: Float, z: Float) extends Event

// Camera ------------------------------------------------------------
final case class CameraMove(transform: Matrix4f) extends Event

// Debug/Random Events -----------------------------------------------
final case class SimpleEvent(data: AnyVal) extends Event

final case class DebugWireframe() extends Event
