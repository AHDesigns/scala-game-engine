package components

import game.GameLogic.loader
import identifier.Identifier
import loaders.{FontLoader, SpriteImage, SpriteSheet}
import org.joml.Vector3f
import rendy.Mesh
import shaders.{ModelShader, SpriteShader}
import utils.Maths.Rot

// ------------------------------------------------
//               IMPLICITS                        -
// ------------------------------------------------
class ComponentId[E] extends Identifier {
  def is(componentId: Identifier): Boolean = {
    this.identifier == componentId.identifier
  }
}

object ComponentId {
  implicit val transform: ComponentId[Transform] = new ComponentId
  implicit val model: ComponentId[Model] = new ComponentId
  implicit val light: ComponentId[Light] = new ComponentId
  implicit val camera: ComponentId[Camera] = new ComponentId
  implicit val playerMovement: ComponentId[PlayerMovement] = new ComponentId
  implicit val cameraActive: ComponentId[CameraActive] = new ComponentId
  implicit val sprite: ComponentId[Sprite] = new ComponentId
  implicit val text: ComponentId[Text] = new ComponentId
}

// ------------------------------------------------
//               COMPONENTS                       -
// ------------------------------------------------
case class Transform(
    var position: Vector3f = new Vector3f(0, 0, 0),
    var rotation: Rot = Rot(),
    var scale: Float = 1f
) extends Component

case class Camera(name: String) extends Component

// TODO: add camera system
case class CameraActive(var activeCamera: Option[String]) extends SingletonComponent

case class PlayerMovement(isCamera: Boolean = false) extends Component

case class Light(color: Vector3f) extends Component

case class Model(mesh: Mesh, shader: ModelShader) extends Component

case class Sprite(private val name: String, private val spriteSheet: SpriteSheet)
    extends Component {
  val spriteImage: SpriteImage = spriteSheet.getSprite(name)
  val mesh: Mesh =
    loader.createMeshSprite(spriteImage, Some(spriteSheet.spriteSize, spriteSheet.spriteSize))
  val shader = new SpriteShader("")
}

object Sprite {
  val defaultSpriteSheet = "sprites.bmp"
}

case class Text(private val string: String)(implicit font: FontLoader) extends Component {
//  val text: IndexedSeq[Glyph] = font.text(string)
//  val mesh: Mesh = loader.createMeshSprite()
//  val shader = new TextShader(50, 68)
}
