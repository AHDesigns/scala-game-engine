package shaders

import loaders.Glyph
import logging.Logger
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20._
import utils.Control.GL
import utils.JavaBufferUtils.getMatrixBuffer

class TextShader(baseLine: Int, lineHeight: Int) extends Shader with Logger {
  val program: Int = load("text") match {
    case Left(err) => logErr(err); 0
    case Right(id) => id
  }

  def draw(
      projModelMatrix: Matrix4f,
      glyph: Glyph
  ): Unit = {
    GL { glUseProgram(program) }

    uniformTexture()

    uniform("projModelMatrix") {
      glUniformMatrix4fv(_, false, getMatrixBuffer(projModelMatrix))
    }

    val Glyph(_, uvStart, uvEnd, size, _, _, _) = glyph

    uniform("glyphOffset") {
      //             x           y           z         w
      glUniform4f(_, uvStart._1, uvEnd._1, uvStart._2, uvEnd._2)
    }

    uniform("glyphSize") {
      glUniform2f(_, size._1, size._2)
    }
  }
}
