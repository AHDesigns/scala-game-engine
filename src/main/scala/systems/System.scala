package systems

import components.{Component, ComponentId, SingletonComponent, World}
import eventSystem.{Event, EventId, EventListener}

import scala.collection.immutable.Queue
import scala.collection.mutable.ArrayBuffer

trait System extends EventListener {
  // ------------------------------------------------
  //              ABSTRACT                          -
  // ------------------------------------------------
  def init(): Unit

  def update(timeDelta: Float): Unit

  // ------------------------------------------------
  //              METHODS                           -
  // ------------------------------------------------
  def processEvents(cb: Event => Unit): Unit = {
    eventQ.foreach(cb)
    eventQ = Queue.empty
  }

  def queueEvents[E <: Event](implicit e: EventId[E]): Unit = events.on[E](queueEvent)

  // TODO: fix B generic to be inferred
  def processComponent[A <: Component, B](
      fn: A => B
  )(implicit componentId: ComponentId[A]): ArrayBuffer[B] = {
    for {
      component <- ecs.getComponentList[A] if component.componentActiveInSystem
    } yield fn(component)
  }

  def getSingleton[A <: SingletonComponent](implicit componentId: ComponentId[A]): A = {
    ecs.getComponentList[A].head
  }

  def getComponent[A <: Component](
      predicate: A => Boolean
  )(implicit componentId: ComponentId[A]): Option[A] = {
    ecs.getComponentList[A].find(predicate)
  }

  // ------------------------------------------------
  //              PRIVATES                          -
  // ------------------------------------------------
  private val ecs = World

  private var eventQ: Queue[Event] = Queue.empty[Event]

  private def queueEvent(e: Event): Unit = eventQ = eventQ :+ e
}
