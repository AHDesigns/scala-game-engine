package ecs

import eventSystem.EventListener
import identifier.{ID, Identifier}

import scala.collection.mutable

class Entity extends EventListener with Identifier {
  private val components: mutable.Set[ID] = mutable.Set.empty

  def hasComponent[A <: Component](implicit component: ComponentId[A]): Boolean =
    components.contains(component.id)

  def addComponent[A <: Component](component: A)(implicit componentId: ComponentId[A]): Entity = {
    components += componentId.id
    component.bind(this)
    this
  }
}
