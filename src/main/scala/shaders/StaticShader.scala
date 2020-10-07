package shaders

import components.{Light, Transform}
import loaders.ShaderLoader
import logging.Logger
import org.joml.{Matrix4f, Vector4f}
import org.lwjgl.opengl.GL20._
import rendy.SpriteOffset
import utils.Control.{GL, GLU}
import utils.JavaBufferUtils.getMatrixBuffer

class StaticShader(color: Vector4f) extends Shader with ShaderLoader with Logger {
  val program: Int = load("basic") match {
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
  ): Unit = {
    // TODO: refactor the get and set into a single method
    val colorLocation = GLU { glGetUniformLocation(program, "aColor") }
    val matrix = GLU { glGetUniformLocation(program, "transformationMatrix") }
    val projectionMatrixLoc = GLU { glGetUniformLocation(program, "projectionMatrix") }
    val viewMatrixLoc = GLU { glGetUniformLocation(program, "viewMatrix") }
    val lightColLoc = GLU { glGetUniformLocation(program, "lightCol") }
    val lightPosLoc = GLU { glGetUniformLocation(program, "lightPos") }

    GL { glUseProgram(program) }
    GL { glUniform4f(colorLocation, color.x, color.y, color.z, color.w) }
    GL { glUniformMatrix4fv(matrix, false, getMatrixBuffer(transformationMatrix)) }
    GL { glUniformMatrix4fv(projectionMatrixLoc, false, getMatrixBuffer(projectionMatrix)) }
    GL { glUniformMatrix4fv(viewMatrixLoc, false, getMatrixBuffer(viewMatrix)) }
    loadVec3(lightPosLoc, lTransform.position)
    loadVec3(lightColLoc, light.color)
  }

  override def draw2D(
      cameraPos: Matrix4f,
      spriteTransform: Matrix4f,
      spriteOffset: SpriteOffset
  ): Unit = ???
}
