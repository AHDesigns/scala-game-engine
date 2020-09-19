package components

import entities.Entity
import eventSystem._
import identifier.ID

import scala.collection.mutable

object ECS extends EventListener {
  // componentId -> (entityId, components[])
  private val ecs: mutable.HashMap[ID, mutable.HashMap[ID, List[_ <: Component]]] =
    mutable.HashMap.empty

  def addComponent[A <: Component](entity: Entity, component: A)(implicit
      componentId: ComponentId[A]
  ): Unit = {
    ecs.get(componentId.id) match {
      case None =>
        ecs.update(componentId.id, mutable.HashMap(entity.id -> List(component)))
      case Some(value) =>
        val newcomps = component :: value.getOrElse(entity.id, Nil)
        value.update(entity.id, newcomps)
    }
    emit(component, entity)
  }

  def emit(component: Component, entity: Entity): Unit = {
    // TODO: find a way to use implicits to handle all these
    component match {
      case c: Transform  => EventSystem ! ComponentTransformCreated(c, entity)
      case c: Model      => EventSystem ! ComponentModelCreated(c)
      case c: Camera     => ;
      case c: Light      => ;
      case c: MeshShader => ;
    }
  }

  type EntityComponents[A] = mutable.HashMap[ID, List[A]]
  def getComponents[A <: Component](implicit
      componentId: ComponentId[A]
  ): Option[EntityComponents[A]] = {
    ecs.get(componentId.id).asInstanceOf[Option[EntityComponents[A]]]
  }
}
