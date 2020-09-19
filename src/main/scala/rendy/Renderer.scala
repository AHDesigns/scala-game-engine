package rendy

import entities.EntityOld
import eventSystem._
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import utils.Control.GL

class Renderer(camera: EntityOld) extends EventListener {
  private var isWireframe = false
  private var projectionMatrix = perspective(800, 400)
//  private var viewMatrix = Maths.createViewMatrix(camera)
  init()

  def init(): Unit = {
    GL { glEnable(GL_DEPTH_TEST) }
    GL { glEnable(GL_CULL_FACE) }
    GL { glCullFace(GL_BACK) }
    events
      .on[WindowResize] { window => projectionMatrix = perspective(window.width, window.height) }
//      .on[CameraMove] { camera => viewMatrix = camera.transform }
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
  }

  private def perspective(w: Int, h: Int) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w.toFloat / h.toFloat,
      0.01f,
      1000f
    )

  /* def render[A <: Entity with Light](entity: Entity, light: A): Unit = {
    viewMatrix = Maths.createViewMatrix(camera)
    entity.mesh.foreach {
      case BasicMesh(vaoID, indices, attributes) =>
        // TODO: store this in entity to save calculating
        val transformationMatrix = Maths.createTransformationMatrix(entity)
        entity.shader.foreach { s =>
          s.draw(transformationMatrix, projectionMatrix, viewMatrix, light)

          GL { glBindVertexArray(vaoID) }

          for (index <- attributes) GL { glEnableVertexAttribArray(index) }

          GL { glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0) }

          for (index <- attributes) GL { glDisableVertexAttribArray(index) }

          GL { glBindVertexArray(0) }
        }
    }
  }*/

  private def wireframe(): Unit = {
    GL { glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE) }
    isWireframe = !isWireframe
  }
}
