package systems.render

import ecs.{Light, Model, Transform}
import eventSystem.{DebugWireframe, EventListener}
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import rendy.BasicMesh
import utils.Control.GL
import utils.Maths

class GLRenderer extends Renderer with EventListener {
  private var isWireframe = false

  def setup(): Unit = {
    GL { glEnable(GL_DEPTH_TEST) }
    GL { glEnable(GL_CULL_FACE) }
    GL { glCullFace(GL_BACK) }
    events.on[DebugWireframe] { wireframe }
  }

  def render(
      camera: Transform,
      light: Light,
      lTransform: Transform,
      model: Model,
      mTransform: Transform,
      projectionMatrix: Matrix4f
  ): Unit = {
    val viewMatrix = Maths.createViewMatrix(camera)
    model.mesh match {
      case BasicMesh(vaoID, indices, attributes) =>
        // TODO: store this in entity to save calculating
        val transformationMatrix = Maths.createTransformationMatrix(mTransform)

        model.shader.draw(
          transformationMatrix,
          projectionMatrix,
          viewMatrix,
          light,
          lTransform
        )

        GL { glBindVertexArray(vaoID) }

        for (index <- attributes) GL { glEnableVertexAttribArray(index) }

        GL { glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0) }

        for (index <- attributes) GL { glDisableVertexAttribArray(index) }

        GL { glBindVertexArray(0) }
    }
  }

  def wireframe(x: DebugWireframe): Unit = {
    GL { glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE) }
    isWireframe = !isWireframe
  }
}
