package systems.render

import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import rendy.BasicMesh
import utils.Control.GL
import utils.Maths

class GLRenderer extends Renderer {
  private var isWireframe = false

  def setup(): Unit = {
    GL { glEnable(GL_DEPTH_TEST) }
    GL { glEnable(GL_CULL_FACE) }
    GL { glCullFace(GL_BACK) }
  }

  def render(
      renderMeshShader: RenderModel,
      light: RenderLight,
      camera: RenderCamera,
      projectionMatrix: Matrix4f
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

  def wireframe(): Unit = {
    GL { glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE) }
    isWireframe = !isWireframe
  }
}
