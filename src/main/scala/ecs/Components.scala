package ecs

import identifier.Identifier
import org.joml.Vector3f
import rendy.Mesh
import shaders.Shader
import utils.Maths.Rot

// Implicits -------------------------------------------------------
object ComponentId {
  implicit val transform: ComponentId[Transform] = new ComponentId
  implicit val model: ComponentId[Model] = new ComponentId
  implicit val light: ComponentId[Light] = new ComponentId
  implicit val camera: ComponentId[Camera] = new ComponentId
  implicit val playerMovement: ComponentId[PlayerMovement] = new ComponentId
}

class ComponentId[E] extends Identifier

sealed trait Component {}

// Components -------------------------------------------------------
case class Transform(
    var position: Vector3f = new Vector3f(0, 0, 0),
    var rotation: Rot = Rot(),
    var scale: Float = 1f
) extends Component

case class Camera(name: String) extends Component

case class PlayerMovement(isCamera: Boolean = false) extends Component

case class Light(color: Vector3f) extends Component

case class Model(mesh: Mesh, shader: Shader) extends Component
