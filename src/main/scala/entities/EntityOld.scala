package entities

import components.Component
import eventSystem.{EventListener, GameLoopTick}
import identifier.ID

import scala.collection.mutable

case class EntityOld(name: String = "", components: List[Component] = List.empty)
    extends EventListener {
  val id: ID = if (name.nonEmpty) ID(name) else ID()
  EntityOld.entities.addOne(id, this)

//  this.components foreach (b => {
//    b.bind(this)
//    b.init()
//  })

  this.events.on[GameLoopTick] { _ =>
//    this.components filter (_.hasUpdate) foreach (_.update())
  }

//  EventSystem ! EntityCreated(this)

  def destroy(): Unit = {
//    this.components foreach (_.destroy())
    EntityOld.entities.remove(id)
    this.events.unsubscribe()
  }

  /** Get a component if it exists on this entity */
  def getComponent[A <: Component](component: Class[_]): Option[A] =
    components
      .find(_.getClass == component)
      .map(_.asInstanceOf[A])
}

object EntityOld {
  lazy val empty = new EntityOld()
  val entities = mutable.HashMap.empty[ID, EntityOld]
}