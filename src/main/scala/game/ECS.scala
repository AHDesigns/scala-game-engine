package game

import java.util.UUID

import eventSystem._
import identifier.Identifier

import scala.collection.immutable.Queue
import scala.collection.mutable.ArrayBuffer

object ECS extends App {
  trait ID {
    def uuid: String = UUID.randomUUID().toString
  }

  class ComponentId[E] extends Identifier
  object ComponentId {
    implicit val transform: ComponentId[Transform] = new ComponentId
    implicit val model: ComponentId[Model] = new ComponentId
  }

  trait Component extends ID {
    var active: Boolean = true
    private var _entityId: Int = 0

    def deactivate(): Unit = { active = false }

    def entityId_=(entityId: Int): Int = {
      if (_entityId == 0) _entityId = entityId
      _entityId
    }

    def entityId: Int = _entityId

    def getSibling[A <: Component](implicit
        componentId: ComponentId[A],
        entityManager: EntityManager
    ): Option[A] = {
      for {
        entity <- entityManager.getEntity(entityId)
        sibling <- entity.getSibling[A](componentId.id)
      } yield sibling
    }
  }

  case class Transform(x: Int, y: Int, z: Int) extends Component
  case class Model(vao: Int) extends Component

  object EmptyComponent extends Component {
    active = false
  }

  class Entity(val entityId: Int)(implicit entityManager: EntityManager) {
    // componentType, (componentUuid, componentIndex)
    var componentMap = Map.empty[Int, Map[String, Int]]
    entityManager.addEntity(entityId, this)

    def addComponent[A <: Component](
        component: A
    )(implicit componentId: ComponentId[A]): this.type = {
      component.entityId = entityId
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
    def addComponent[A <: Component](component: A)(implicit componentId: ComponentId[A]): Int = {
      val componentType = componentId.id
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

  trait System extends EventListener {
    protected def init(): Unit
    protected def update(timeDelta: Float): Unit

    private var eventQ: Queue[Event] = Queue.empty[Event]

    private def queueEvent(e: Event): Unit = eventQ = eventQ :+ e

    def processEvents(cb: Event => Unit): Unit = {
      eventQ.foreach(cb)
      eventQ = Queue.empty
    }

    def queueEvents[E <: Event](implicit e: EventId[E]): Unit = events.on[E](queueEvent)
  }

  object RenderSystem extends System {
    override def init(): Unit = {
      queueEvents[GameLoopStart]
      queueEvents[GameLoopTick]
    }

    override def update(timeDelta: Float): Unit = {
      for {
        model <- ecs.getComponentList[Model] if model.active
        transform <- model.getSibling[Transform]
      } {
        println(transform, model)
        processEvents {
          case _: GameLoopStart => println("hi");
          case _                => ;
        }
      }

    }
  }

  implicit val ecs: EntityManager = new EntityManager(0)
  val e1 = new Entity(0)
    .addComponent(Transform(1, 2, 3))
    .removeComponent[Model]()

  val e2 = new Entity(1)
    .addComponent(Transform(3, 2, 1))
    .addComponent(Model(3))

  RenderSystem.init()
  EventSystem ! GameLoopStart()
  RenderSystem.update(3f)
  RenderSystem.update(3f)
  EventSystem ! GameLoopStart()
  e2.removeComponent[Model]()
  e2.addComponent(Model(4))
  RenderSystem.update(3f)

}
