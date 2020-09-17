package ecs

import eventSystem.{ComponentModelCreated, ComponentTransformCreated, EventSystem}
import identifier.{ID, Identifier}

import scala.collection.mutable

sealed trait Component {
  def bind(entity: Entity): Unit
}

trait ComponentObject[A <: Component] {
  final val data = mutable.HashMap.empty[ID, A]
}

case class Transform(x: Int, y: Int) extends Component {
  def bind(entity: Entity): Unit = {
    Transform.data += entity.id -> this
    println("creating transform")
    EventSystem ! ComponentTransformCreated(this, entity)
  }
}

object Transform extends ComponentObject[Transform] {}

case class Model(mesh: String, shader: Float) extends Component {
  def bind(entity: Entity): Unit = {
    Model.data += entity.id -> this
    EventSystem ! ComponentModelCreated(this)
  }
}

object Model extends ComponentObject[Model] {}

class ComponentId[E] extends Identifier
object ComponentId {
  implicit val transform: ComponentId[Transform] = new ComponentId
  implicit val model: ComponentId[Model] = new ComponentId
}
