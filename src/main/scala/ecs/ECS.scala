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
    emit(component, entity)

    /** this only exists for pattern matching */
    def emit(component: Component, entity: Entity): Unit = {
      // TODO: find a way to use implicits to handle all these
      component match {
        case c: Transform      => EventSystem ! ComponentCreatedTransform(c, entity)
        case c: Model          => EventSystem ! ComponentCreatedModel(c, entity)
        case c: Camera         => EventSystem ! ComponentCreatedCamera(c, entity)
        case c: Light          => EventSystem ! ComponentCreatedLight(c, entity)
        case c: PlayerMovement => EventSystem ! ComponentCreatedPlayerMovement(c, entity)
        case c: RigidBody      => EventSystem ! ComponentCreatedRigidBody(c, entity)
        case c: Collider       => EventSystem ! ComponentCreatedCollider(c, entity)
      }
    }
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
}
