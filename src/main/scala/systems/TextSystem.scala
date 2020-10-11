package systems

import components.{Camera, CameraActive, Text, Transform}
import org.joml.Matrix4f
import systems.render.Renderer
import utils.Maths

class TextSystem(renderer: Renderer) extends System {
  private var projectionMatrix = perspective(800, 400)
  override def init(): Unit = {
    renderer.setup()
  }

  override def update(timeDelta: Float): Unit = {
    val camera = for {
      cameraId <- getSingleton[CameraActive].activeCamera
      camera <- getComponent[Camera] { _.name == cameraId }
      transform <- camera.getSibling[Transform]
    } yield transform

    for { cTransform <- camera } {
      val cMatrix = perspective(800, 400).mul(Maths.createViewMatrix(cTransform))
      processComponent[Text, Unit] { sprite =>
        for { sTransform <- sprite.getSibling[Transform] } {
          val spriteMatrix = Maths.createTransformationMatrix(sTransform)
//          renderer.renderText(cMatrix, spriteMatrix, sprite)
        }
      }
    }
  }

  private def perspective(w: Int, h: Int) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w.toFloat / h.toFloat,
      0.01f,
      1000f
    )
}
