package ecs

import ecs.colliders.Colliders
import identifier.{ID, Identifier}
import observer.ObserverSystem
import org.joml.Vector3f
import rendy.Mesh
import shaders.Shader
import utils.Maths.Rot
import utils.{Axis, Directions}

// Implicits -------------------------------------------------------
object ComponentId {
  implicit val transform: ComponentId[Transform] = new ComponentId
  implicit val model: ComponentId[Model] = new ComponentId
  implicit val light: ComponentId[Light] = new ComponentId
  implicit val camera: ComponentId[Camera] = new ComponentId
  implicit val playerMovement: ComponentId[PlayerMovement] = new ComponentId
  implicit val collider: ComponentId[Collider] = new ComponentId
  implicit val rigidBody: ComponentId[RigidBody] = new ComponentId
  implicit val delete: ComponentId[Delete] = new ComponentId
}

class ComponentId[E] extends Identifier {
  def is(componentId: Identifier): Boolean = {
    this.identifier == componentId.identifier
  }
}

sealed trait Component {
  var entityId: ID = ID()
  def init(entityID: ID): Unit = entityId = entityID
}

object ComponentSystem extends ObserverSystem[Component, ComponentId]

trait ComponentObserver {
  val components: ComponentSystem.Observer = ComponentSystem.subscribe()

  /** Takes a componentId followed by a callback fn which is only invoked if said component is deleted */
  def delIs(id: Identifier)(fn: Delete => Unit)(delete: Delete): Unit =
    delete match {
      case Delete(_, componentId) => if (componentId.is(id)) fn(delete)
    }
}
// Components -------------------------------------------------------
class Delete(val componentId: ComponentId[_]) extends Component

object Delete {
  def apply(id: ID, componentId: ComponentId[_]): Delete = {
    val delete = new Delete(componentId)
    delete.init(id)
    delete
  }
  def unapply(arg: Delete): Option[(ID, ComponentId[_])] = Some(arg.entityId, arg.componentId)
}

case class Transform(
    var position: Vector3f = new Vector3f(0, 0, 0),
    var rotation: Rot = Rot(),
    var scale: Float = 1f
) extends Component

case class Camera(name: String) extends Component

case class PlayerMovement(isCamera: Boolean = false) extends Component

case class Light(color: Vector3f) extends Component

case class Model(mesh: Mesh, shader: Shader) extends Component

case class Collider(shape: Colliders) extends Component

case class RigidBody(constrain: Option[Seq[Axis.Val]] = None, gravity: Boolean = true)
    extends Component {
//  var acceleration: Vector3f = Directions.None.toVec

  /** The initial velocity of the RigidBody */
  var vInitial: Vector3f = Directions.None.toVec

  /** The final velocity of the RigidBody */
  var vFinal: Vector3f = Directions.None.toVec
}
