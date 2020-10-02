package ecs

import eventSystem._
import identifier.ID

import scala.collection.mutable

// TODO: change ECS to use unboxed Arrays not Maps
// after some googling, it seems using hash lookups could be really slow, and it might be
// best to swap to Arrays, using integer IDs for lookups
//  https://www.lihaoyi.com/post/BenchmarkingScalaCollections.html

/** Entity Component System Database
  *
  * All components actually live in here, grouped by ComponentId and then stored against their EntityID
  *
  * componentId -> (entityId, components[])[]
  */
object ECS extends EventListener {
  // componentId -> (entityId, components[])
  private val ecs: mutable.HashMap[ID, mutable.HashMap[ID, List[_ <: Component]]] =
    mutable.HashMap.empty
  private val entities = mutable.HashMap.empty[ID, Entity]

  def addEntity(entity: Entity): Unit = {
    entities += (entity.id -> entity)
  }
  def removeEntity(entity: Entity): Unit = {
    entities -= entity.id
  }

  def removeComponent[A <: Component](entity: Entity, component: ComponentId[A]): Unit = {
    // TODO: support removing single components from component list
    ecs.get(component.id) match {
      case Some(ls) => ls.remove(entity.id)
      case _        => ;
    }

    ComponentSystem ! Delete(entity.id, component)
  }

  /** Add a component to the ECS
    *
    * supports duplicate components and these will be grouped into a List
    */
  def addComponent[A <: Component](entity: Entity, component: A)(implicit
      componentId: ComponentId[A]
  ): Unit = {
    ecs.get(componentId.id) match {
      case None =>
        ecs.update(componentId.id, mutable.HashMap(entity.id -> List(component)))
      case Some(value) =>
        val newComps = component :: value.getOrElse(entity.id, Nil)
        value.update(entity.id, newComps)
    }

    ComponentSystem ! component
  }

  // I thought I could share this type throughout the class but it doesn't seem to work in all places
  type EntityComponents[A] = mutable.HashMap[ID, List[A]]

  /** Get all components of a given Type
    *
    * e.g getComponents[Transform]
    *
    * returns a map of (entityID -> components)
    */
  def getComponents[A <: Component](implicit
      componentId: ComponentId[A]
  ): Option[EntityComponents[A]] = {
    ecs.get(componentId.id).asInstanceOf[Option[EntityComponents[A]]]
  }

  def demandComponents[A <: Component](implicit
      componentId: ComponentId[A]
  ): EntityComponents[A] = {
    ecs
      .getOrElse(componentId.id, throw new RuntimeException("could not get components!"))
      .asInstanceOf[EntityComponents[A]]
  }

  /** Get component(s) from an Entity */
  def getEntityComponent[A <: Component](
      entity: Entity
  )(implicit componentId: ComponentId[A]): Option[List[A]] = {
    ecs.get(componentId.id).asInstanceOf[Option[EntityComponents[A]]] match {
      case Some(entityComponents) => entityComponents.get(entity.id)
      case None                   => None
    }
  }

  def getEntity(entityId: ID): Option[Entity] = {
    entities.get(entityId)
  }
}
