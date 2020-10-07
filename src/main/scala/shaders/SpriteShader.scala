package shaders

import components.{Light, Transform}
import loaders.ShaderLoader
import logging.Logger
import org.joml.Matrix4f
import org.lwjgl.opengl.GL20._
import rendy.SpriteOffset
import utils.Control.GL
import utils.JavaBufferUtils.getMatrixBuffer

class SpriteShader(shaderName: String) extends Shader with ShaderLoader with Logger {
  val program: Int = load("sprite") match {
    case Left(err) => logErr(err); 0
    case Right(id) => id
  }

  def draw(
      transformationMatrix: Matrix4f,
      projectionMatrix: Matrix4f,
      viewMatrix: Matrix4f,
      light: Light,
      lTransform: Transform,
      textureId: Option[Int]
  ): Unit = {}

  override def draw2D(
      cameraPos: Matrix4f,
      spriteMatrix: Matrix4f,
      spriteOffset: SpriteOffset
  ): Unit = {
    GL { glUseProgram(program) }

    uniformTexture()

    uniform("cameraMatrix") {
      glUniformMatrix4fv(_, false, getMatrixBuffer(cameraPos))
    }
    uniform("spriteMatrix") {
      glUniformMatrix4fv(_, false, getMatrixBuffer(spriteMatrix))
    }
    uniform("spriteOffset") {
      val SpriteOffset(x1, x2, y1, y2) = spriteOffset
      //             x   y   z   w
      glUniform4f(_, x1, x2, y1, y2)
    }
  }
}
