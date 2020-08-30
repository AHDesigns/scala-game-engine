package rendy

import entities.Entity
import eventSystem.{DebugWireframe, Events, GameEnd, WindowResize}
import org.joml.Matrix4f
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import utils.Control.GL
import utils.Maths

class Renderer() extends Events {
  private var isWireframe = false
  private var projectionMatrix = perspective(800, 400)
  init()

  def init(): Unit = {
    events
      .on[WindowResize] { window => projectionMatrix = perspective(window.width, window.height) }
      .on[DebugWireframe] { _ => wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
  }

  private def perspective(w: Float, h: Float) = new Matrix4f().setPerspective(
    Math.toRadians(70).toFloat,
    (w / h).toFloat,
    0.01f,
    1000f
  )

  def render(entity: Entity): Unit = {

    entity.model match {
      case BasicModel(vaoID, indices, attributes) =>
        val transformationMatrix = Maths.createTransformationMatrix(entity.position, entity.rotation, entity.scale)
        entity.shader.draw(transformationMatrix, projectionMatrix)
        GL {
          glBindVertexArray(vaoID)
        }

        for (index <- 0 until attributes) GL {
          glEnableVertexAttribArray(index)
        }

        GL {
          glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0)
        }

        for (index <- (attributes - 1) to 0) GL {
          glDisableVertexAttribArray(index)
        }

        GL {
          glBindVertexArray(0)
        }
    }
  }

  private def wireframe(): Unit = {
    GL {
      glPolygonMode(GL_FRONT_AND_BACK, if (isWireframe) GL_FILL else GL_LINE)
    }
    isWireframe = !isWireframe
  }
}
