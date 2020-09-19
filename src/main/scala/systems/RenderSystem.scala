package systems

import components.{Camera, Light, MeshShader, Transform}
import eventSystem._
import identifier.ID
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import rendy.BasicMesh
import utils.Control.GL
import utils.Maths

import scala.collection.mutable

case class RenderMeshShader(model: MeshShader, transform: Transform)
case class RenderLight(light: Light, transform: Transform)
case class RenderCamera(camera: Camera, transform: Transform)

object RenderSystem extends EventListener {

  private var isWireframe = false
  private var projectionMatrix = perspective(800, 400)
  private var activeCamera: ID = _
  // private var viewMatrix = Maths.createViewMatrix(camera)

  // TODO: quick first pass
  val transforms = mutable.HashMap.empty[ID, Transform]
  val models = mutable.HashMap.empty[ID, MeshShader]
  val cameras = mutable.HashMap.empty[ID, Camera]
  val lights = mutable.HashMap.empty[ID, Light]

  def update(): Unit = {}

  def init(): Unit = {
    GL { glEnable(GL_DEPTH_TEST) }
    GL { glEnable(GL_CULL_FACE) }
    GL { glCullFace(GL_BACK) }
    events
      .on[WindowResize] { window => projectionMatrix = perspective(window.width, window.height) }
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
//      .on[ComponentCreated] { e =>
//        e.component match {
//          case comp: Camera =>
//            cameras += ((comp.me.id, comp))
//            activeCamera = comp.me.id
//          case comp: Light      => lights += ((comp.me.id, comp))
//          case comp: MeshShader => models += ((comp.me.id, comp))
//          case comp: Transform  => transforms += ((comp.me.id, comp))
//          case _                => ;
//        }
//      }
    //    .on[CameraMove] { camera => viewMatrix = camera.transform }
  }

  private def perspective(w: Int, h: Int) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w.toFloat / h.toFloat,
      0.01f,
      1000f
    )

  def renderAll(): Unit = {
    val camera = RenderCamera(
      cameras.getOrElse(activeCamera, throw new RuntimeException("no active camera")),
      transforms.getOrElse(activeCamera, throw new RuntimeException("no transform for camera"))
    )
    val (id, lightModel) = lights.find(_._1.id > 0).get
    val light = RenderLight(
      lightModel,
      transforms.getOrElse(id, throw new RuntimeException("no transform for light"))
    )
    models foreach {
      case (id, model) =>
        val rms = RenderMeshShader(
          model,
          transforms.getOrElse(id, throw new RuntimeException(s"no transform for $id"))
        )
        render(rms, light, camera)
    }
  }

  private def wireframe(): Unit = {
    GL { glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE) }
    isWireframe = !isWireframe
  }

  private def render(
      renderMeshShader: RenderMeshShader,
      light: RenderLight,
      camera: RenderCamera
  ): Unit = {
    val viewMatrix = Maths.createViewMatrix(camera.transform)
    renderMeshShader.model.mesh match {
      case BasicMesh(vaoID, indices, attributes) =>
        // TODO: store this in entity to save calculating
        val transformationMatrix = Maths.createTransformationMatrix(renderMeshShader.transform)

        renderMeshShader.model.shader.draw(
          transformationMatrix,
          projectionMatrix,
          viewMatrix,
          light
        )

        GL { glBindVertexArray(vaoID) }

        for (index <- attributes) GL { glEnableVertexAttribArray(index) }

        GL { glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0) }

        for (index <- attributes) GL { glDisableVertexAttribArray(index) }

        GL { glBindVertexArray(0) }
    }
  }

}
