package rendy

import entities.{Entity, Light}
import eventSystem._
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{
  glDisableVertexAttribArray,
  glEnableVertexAttribArray
}
import org.lwjgl.opengl.GL30.glBindVertexArray
import utils.Control.GL
import utils.Maths

class Renderer() extends Events {
  private var isWireframe = false
  private var projectionMatrix = perspective(800, 400)
  private var viewMatrix = new Matrix4f().identity()
  init()

  def init(): Unit = {
    GL(glEnable(GL_DEPTH_TEST))
    events
      .on[WindowResize] { window =>
        projectionMatrix = perspective(window.width, window.height)
      }
      .on[CameraMove] { camera => viewMatrix = camera.transform }
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
  }

  private def perspective(w: Float, h: Float) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w / h,
      0.01f,
      1000f
    )

  def render(entity: Entity, light: Light): Unit = {

    entity.model match {
      case BasicModel(vaoID, indices, attributes) =>
        val transformationMatrix = Maths.createTransformationMatrix(
          entity.position,
          entity.rotation,
          entity.scale
        )
        entity.shader.draw(
          transformationMatrix,
          projectionMatrix,
          viewMatrix,
          light
        )
        GL(glBindVertexArray(vaoID))

        for (index <- attributes) GL(glEnableVertexAttribArray(index))

        GL(glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0))

        for (index <- attributes) GL(glDisableVertexAttribArray(index))

        GL(glBindVertexArray(0))
    }
  }

  private def wireframe(): Unit = {
    GL(glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE))
    isWireframe = !isWireframe
  }
}
