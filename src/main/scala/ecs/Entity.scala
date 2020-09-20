package ecs

import eventSystem.EventListener
import identifier.{ID, Identifier}

import scala.collection.mutable

class Entity(val name: String = "") extends EventListener with Identifier {
  override val id: ID = if (name.nonEmpty) ID(name) else ID()
  private val components: mutable.Set[ID] = mutable.Set.empty

  def addComponent[A <: Component](component: A)(implicit componentId: ComponentId[A]): Entity = {
    components += componentId.id
    ECS.addComponent(this, component)
    this
  }

  def getComponent[A <: Component](implicit componentId: ComponentId[A]): Option[List[A]] = {
    if (this.hasComponent[A]) ECS.getEntityComponent(this)
    else None
  }

  def hasComponent[A <: Component](implicit component: ComponentId[A]): Boolean =
    components.contains(component.id)

  def hasComponents[A <: Component, B <: Component](implicit
      component: ComponentId[A],
      component2: ComponentId[B]
  ): Boolean =
    List(component, component2).map(_.id).toSet.subsetOf(components)

  def hasComponents[A <: Component, B <: Component, C <: Component](implicit
      component: ComponentId[A],
      component2: ComponentId[B],
      component3: ComponentId[C]
  ): Boolean =
    List(component, component2, component3).map(_.id).toSet.subsetOf(components)

  def hasComponents[A <: Component, B <: Component, C <: Component, D <: Component](implicit
      component: ComponentId[A],
      component2: ComponentId[B],
      component3: ComponentId[C],
      component4: ComponentId[D]
  ): Boolean =
    List(
      component,
      component2,
      component3,
      component4
    ).map(_.id).toSet.subsetOf(components)
}
