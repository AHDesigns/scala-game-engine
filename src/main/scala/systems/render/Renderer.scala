package systems.render

import components._
import eventSystem.{DebugWireframe, EventListener}
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13.{glActiveTexture, GL_TEXTURE0}
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import rendy.BasicMesh
import utils.Control.GL
import utils.Maths

class Renderer extends EventListener {
  private var isWireframe = false

  def setup(): Unit = {
//    GL { glEnable(GL_DEPTH_TEST) }
//    GL { glEnable(GL_CULL_FACE) }
//    GL { glCullFace(GL_BACK) }

    GL { glEnable(GL_BLEND) }
    GL { glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA) }
    events.on[DebugWireframe] { wireframe }
  }

//  private def featureSet(featuresToEnable: Seq[Int])(renderFn: => Unit) = {
//
//  }

  def renderModel(
      camera: Transform,
      light: Light,
      lTransform: Transform,
      model: Model,
      mTransform: Transform,
      projectionMatrix: Matrix4f
  ): Unit = {
    val viewMatrix = Maths.createViewMatrix(camera)
    model.mesh match {
      case BasicMesh(vaoID, indices, attributes, textureId) =>
        // TODO: store this in entity to save calculating
        val transformationMatrix = Maths.createTransformationMatrix(mTransform)

        for { id <- textureId } {
          GL { glActiveTexture(GL_TEXTURE0) }
          GL { glBindTexture(GL_TEXTURE_2D, id) }
        }

        model.shader.draw(
          transformationMatrix,
          projectionMatrix,
          viewMatrix,
          light,
          lTransform,
          textureId
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

  def renderSprite(spriteMatrix: Matrix4f, sprite: Sprite): Unit = {
    sprite.mesh match {
      case BasicMesh(vaoID, indices, attributes, textureId) =>
        for { id <- textureId } {
          GL { glActiveTexture(GL_TEXTURE0) }
          GL { glBindTexture(GL_TEXTURE_2D, id) }
        }

        sprite.shader.draw(spriteMatrix)

        GL { glBindVertexArray(vaoID) }

        for (index <- attributes) GL { glEnableVertexAttribArray(index) }

        GL { glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0) }

        for (index <- attributes) GL { glDisableVertexAttribArray(index) }

        GL { glBindVertexArray(0) }
    }
  }

  def renderText(textTransform: Matrix4f, text: Text): Unit = {
    var advance = 0f
    var lastChar = ' '

    text.text.foreach {
      case (glyph, BasicMesh(vaoID, indices, attributes, textureId)) =>
        for { id <- textureId } {
          GL { glActiveTexture(GL_TEXTURE0) }
          GL { glBindTexture(GL_TEXTURE_2D, id) }
        }

        GL { glBindVertexArray(vaoID) }

        for (index <- attributes) GL { glEnableVertexAttribArray(index) }

        val kerning = text.font.getKerning(lastChar, glyph.char)
        text.shader.draw(
          textTransform.translate(
            new Vector3f(advance + kerning, 0, 0),
            new Matrix4f()
          )
        )

        advance += glyph.xAdvance
        lastChar = glyph.char

        GL { glDrawElements(GL_TRIANGLES, indices, GL_UNSIGNED_INT, 0) }

        for (index <- attributes) GL { glDisableVertexAttribArray(index) }

        GL { glBindVertexArray(0) }
    }
  }
}
