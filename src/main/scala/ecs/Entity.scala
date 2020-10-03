package ecs

import identifier.{ID, Identifier}

class Entity(val name: String = "") extends Identifier {
  override val identifier: ID = if (name.nonEmpty) ID(name) else ID()

  private val entityManager = EntityManager
  // componentType, (componentUuid, componentIndex)
  var componentMap = Map.empty[Int, Map[String, Int]]
  entityManager.addEntity(id, this)

  def addComponent[A <: Component](
      component: A
  )(implicit componentId: ComponentId[A]): this.type = {
    component.entityId = id
    val componentType = componentId.id
    val pos = entityManager.addComponent(component)
    val componentList = componentMap.getOrElse(componentType, Map.empty)
    val newComponent = component.uuid -> pos
    componentMap += (componentType -> (componentList + newComponent))
    this
  }

  def getSiblings[A <: Component](componentId: Int): Option[Seq[A]] = {
    for {
      componentMap <- componentMap.get(componentId)
      sibling <- entityManager.getComponents[A](componentId, componentMap.values.toList)
    } yield sibling
  }

  def getSibling[A <: Component](componentId: Int): Option[A] = {
    for {
      componentMap <- componentMap.get(componentId)
      componentArrayIdx = componentMap.values.head
      sibling <- entityManager.getComponents[A](componentId, Seq(componentArrayIdx))
    } yield sibling.head
  }

  def demandSiblings[A <: Component](componentId: Int): Seq[A] =
    getSiblings[A](componentId).getOrElse(
      throw new RuntimeException(s"could not get component $componentId")
    )

  def demandSibling[A <: Component](componentId: Int): A = demandSiblings[A](componentId).head

  def removeComponent[A <: Component](
      componentUuid: Option[String] = None
  )(implicit componentId: ComponentId[A]): this.type = {
    val componentType = componentId.id
    val componentArrayIdx = componentMap.get(componentType) match {
      case None => None
      case Some(components) =>
        componentUuid match {
          case Some(uuid) =>
            val componentArrayIdx = components.get(uuid)
            val componentList = componentMap.getOrElse(componentType, Map.empty)
            componentMap += (componentType -> (componentList - uuid))
            componentArrayIdx
          case None =>
            val componentArrayIdx = components.headOption.map(_._2)
            componentMap -= componentType
            componentArrayIdx
        }
    }
    for { idx <- componentArrayIdx } entityManager.removeComponent(componentType, idx)
    this
  }
}
