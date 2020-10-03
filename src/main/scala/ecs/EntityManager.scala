package ecs

import scala.collection.mutable.ArrayBuffer

object EntityManager {
  // entityId, Entity
  private var entities = Map.empty[Int, Entity]
  // componentId, components
  private var components = Map.empty[Int, ArrayBuffer[_ >: Component]]
  // componentId, idx
  private var emptySlots = Map.empty[Int, List[Int]]

  def getEntity(entityId: Int): Option[Entity] = entities.get(entityId)

  def addEntity(entityId: Int, entity: Entity): Unit = {
    entities += (entityId -> entity)
  }

  /** add component and return position in array */
  def addComponent[A <: Component](component: A)(implicit componentId: ComponentId[A]): Int = {
    val componentType = componentId.id

    if (component.singleton) {
      for { _ <- components.get(componentType) } yield return 0
      components += (componentType -> ArrayBuffer.empty.addOne(component))
      return 0
    }

    val componentList = components.getOrElse(componentType, ArrayBuffer.empty)
    emptySlots.get(componentType) match {
      case Some(emptySlot :: remainingSlots) =>
        componentList(emptySlot) = component
        components += (componentType -> componentList)
        emptySlots = emptySlots.updated(componentType, remainingSlots)
        emptySlot
      case None | Some(Nil) =>
        components += (componentType -> componentList.addOne(component))
        componentList.length - 1
    }
  }

  def removeComponent(componentType: Int, componentArrayIdx: Int): Unit = {
    for { componentList <- components.get(componentType) } {
      componentList(componentArrayIdx).asInstanceOf[Component].deactivate()
    }
    emptySlots += (componentType -> (componentArrayIdx :: emptySlots.getOrElse(
      componentType,
      Nil
    )))
  }

  def getComponentList[A <: Component](implicit componentId: ComponentId[A]): ArrayBuffer[A] = {
    val componentType = componentId.id
    components
      .getOrElse(
        componentType,
        throw new RuntimeException(s"no components of type $componentType")
      )
      .asInstanceOf[ArrayBuffer[A]]
  }

  def getComponents[A <: Component](
      componentType: Int,
      componentArrayIdx: Seq[Int]
  ): Option[Seq[A]] = {
    for {
      componentList <- components.get(componentType)
    } yield {
      componentArrayIdx
        .map(componentList(_).asInstanceOf[A])
    }
  }
}
