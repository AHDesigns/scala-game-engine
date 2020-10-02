package game

import java.util.UUID

import scala.collection.mutable.ArrayBuffer

object ECS extends App {
  trait ID { lazy val uuid = new UUID(64, 64) }

  object Component {
    implicit val transform: Int = 1
    implicit val model: Int = 2
  }

  abstract class Component(val componentId: Int) extends ID {
    var active: Boolean = true
    private var _entityId: Int = 0

    def entityId_=(entityId: Int): Int = {
      if (_entityId == 0) _entityId = entityId
      _entityId
    }

    def entityId: Int = _entityId

    def getSibling[A <: Component](
        componentType: Int
    )(implicit entityManager: EntityManager): Option[A] = {
      for {
        entity <- entityManager.getEntity(entityId)
        sibling <- entity.getSibling[A](componentType)
      } yield sibling
    }
  }

  case class Transform(x: Int, y: Int, z: Int) extends Component(Component.transform)
  case class Model(vao: Int) extends Component(Component.model)

  object EmptyComponent extends Component(0) {
    active = false
  }

  class Entity(val entityId: Int)(implicit entityManager: EntityManager) {
    // componentType, (componentUuid, componentIndex)
    var componentMap = Map.empty[Int, Map[UUID, Int]]
    entityManager.addEntity(entityId, this)

    def addComponent(component: Component): this.type = {
      component.entityId = entityId
      val componentType = component.componentId
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

    def removeComponent(componentType: Int, componentUuid: Option[UUID] = None): this.type = {
      val componentArrayIdx = componentMap.get(componentType) match {
        case None =>
          println("missing component")
          None
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

  class EntityManager(id: Int) {
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
    def addComponent[A <: Component](component: A): Int = {
      val componentType = component.componentId
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
      for { componentList <- components.get(componentType) } componentList(componentArrayIdx) =
        EmptyComponent
      emptySlots += (componentType -> (componentArrayIdx :: emptySlots.getOrElse(
        componentType,
        Nil
      )))
    }

    def getComponentList[A <: Component](componentType: Int): ArrayBuffer[A] = {
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
        componentArrayIdx.map(componentList(_).asInstanceOf[A])
      }
    }
  }

  implicit val ecs: EntityManager = new EntityManager(0)
  val e1 = new Entity(0).addComponent(Transform(1, 2, 3))

  val e2 = new Entity(1)
    .addComponent(Transform(3, 2, 1))
    .addComponent(Model(3))
    .removeComponent(Component.model)

  println(e1)
  println(e2)

  for {
    transform <- ecs.getComponentList[Transform](Component.transform)
    model <- transform.getSibling[Model](Component.model)
  } {
    println(transform, model)
  }
}
