package rendy

import eventSystem.{DebugWireframe, Events, GameEnd}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray

import scala.annotation.tailrec

class Renderer extends Events {
  private var isWireframe = false
  init()

  def init(): Unit = {
    events
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
  }

  def GL[T](fn: => T): T = {
    clearErrors
    val res = fn
    val err = glGetError()
    if (err != 0) {
      new RuntimeException(s"GL error $err").printStackTrace()
      System.exit(1)
    }
    res
  }

  @tailrec
  private def clearErrors: Int = if (glGetError() == 0) 0 else clearErrors

  def render(model: Model): Unit = {
    model match {
      case BasicModel(vaoID, indices) =>
        glBindVertexArray(vaoID)

        glEnableVertexAttribArray(0)
        glEnableVertexAttribArray(1)

        GL {
          glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0)
        }

        glDisableVertexAttribArray(0)
        glDisableVertexAttribArray(1)
        glBindVertexArray(0)
    }
  }

  private def wireframe(): Unit = {
    glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE)
    isWireframe = !isWireframe
  }
}
