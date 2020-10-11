package systems

import components._
import eventSystem.{GameLoopStart, GameLoopTick}
import org.joml.Matrix4f
import systems.render.Renderer

class RenderSystem(renderer: Renderer) extends System {
  private var projectionMatrix = perspective(800, 400)

  override def init(): Unit = {
    renderer.setup()
    queueEvents[GameLoopStart]
    queueEvents[GameLoopTick]
  }

  override def update(timeDelta: Float): Unit = {
    val camera = for {
      cameraId <- getSingleton[CameraActive].activeCamera
      camera <- getComponent[Camera] { _.name == cameraId }
      transform <- camera.getSibling[Transform]
    } yield transform

    val sun = processComponent[Light, Option[(Light, Transform)]] { light =>
      for { transform <- light.getSibling[Transform] } yield (light, transform)
    }.head

    for {
      cTransform <- camera
      (sun, sTransform) <- sun
    } {
      processComponent[Model, Unit] { model =>
        for { mTransform <- model.getSibling[Transform] } {
          renderer.renderModel(cTransform, sun, sTransform, model, mTransform, projectionMatrix)
        }
      }
    }
  }

  // ------------------------------------------------
  //              PRIVATES                          -
  // ------------------------------------------------
  private def getActiveCamera: Option[String] = {
    getSingleton[CameraActive].activeCamera
//    var activeCamera: Option[String] = None
//    processComponent[CameraActive] {
//      case CameraActive(active) => activeCamera = active
//    }
//    activeCamera
  }

  private def perspective(w: Int, h: Int) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w.toFloat / h.toFloat,
      0.01f,
      1000f
    )

}
