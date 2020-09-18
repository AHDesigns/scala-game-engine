package ecs

import eventSystem.{ComponentModelCreated, ComponentTransformCreated, EventListener, EventSystem}
import identifier.{ID, Identifier}

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
    component match {
      case c: Transform => EventSystem ! ComponentTransformCreated(c, entity)
      case c: Model     => EventSystem ! ComponentModelCreated(c)
    }
  }

  type EntityComponents[A] = mutable.HashMap[ID, List[A]]
  def getComponents[A <: Component](implicit
      componentId: ComponentId[A]
  ): Option[EntityComponents[A]] = {
    ecs.get(componentId.id).asInstanceOf[Option[EntityComponents[A]]]
  }
}

sealed trait Component {}

case class Transform(x: Int, y: Int) extends Component
case class Model(mesh: String, shader: Float) extends Component

class ComponentId[E] extends Identifier
object ComponentId {
  implicit val transform: ComponentId[Transform] = new ComponentId
  implicit val model: ComponentId[Model] = new ComponentId
}
