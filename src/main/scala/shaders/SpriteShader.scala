package shaders

import logging.Logger
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20._
import utils.Control.GL
import utils.JavaBufferUtils.getMatrixBuffer

class SpriteShader(shaderName: String) extends Shader with Logger {
  val program: Int = load("sprite") match {
    case Left(err) => logErr(err); 0
    case Right(id) => id
  }

  def draw(spriteMatrix: Matrix4f): Unit = {
    GL { glUseProgram(program) }

    uniformTexture(0)

    uniform("spriteMatrix") {
      glUniformMatrix4fv(_, false, getMatrixBuffer(spriteMatrix))
    }
  }
}
