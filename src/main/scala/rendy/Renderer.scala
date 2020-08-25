package rendy

import org.lwjgl.opengl.GL11.{GL_TRIANGLES, GL_UNSIGNED_INT, glDrawElements}
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray

class Renderer {
  def render(model: Model): Unit = {
    model match {
      case RawModel(vaoID, indices) =>
        glBindVertexArray(vaoID)

        glEnableVertexAttribArray(0)
        glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0)

        glDisableVertexAttribArray(0)
        glBindVertexArray(0)
    }
  }

}
