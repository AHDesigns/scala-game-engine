package rendy

import eventSystem.{DebugWireframe, Events, GameEnd}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray

class Renderer extends Events {
  private var isWireframe = false
  init()

  def init(): Unit = {
    events
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
  }

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

  private def wireframe(): Unit = {
    glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE)
    isWireframe = !isWireframe
  }
}
