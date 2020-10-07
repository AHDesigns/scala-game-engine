package shaders

import components.{Light, Transform}
import loaders.ShaderLoader
import logging.Logger
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL20._
import rendy.SpriteOffset
import utils.Control.{GL, GLU}
import utils.JavaBufferUtils.getMatrixBuffer

class ColorShader(shaderName: String) extends Shader with ShaderLoader with Logger {
  val program: Int = load(shaderName) match {
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
    val time = GL { glfwGetTime() }
    val greenValue = (Math.sin(time).toFloat / 2f) + 0.5f
    //    val colorLocation = GLU(glGetUniformLocation(program, "aColor"))
    val matrix = GLU { glGetUniformLocation(program, "transformationMatrix") }
    val projectionMatrixLoc = GLU { glGetUniformLocation(program, "projectionMatrix") }
    val viewMatrixLoc = GLU { glGetUniformLocation(program, "viewMatrix") }
    // val lightColLoc = GLU(glGetUniformLocation(program, "lightCol"))
    // val lightPosLoc = GLU(glGetUniformLocation(program, "lightPos"))

    GL { glUseProgram(program) }
    //    GL { glUniform4f(colorLocation, 0.0f, greenValue, 0.0f, 1.0f) }
    GL { glUniformMatrix4fv(matrix, false, getMatrixBuffer(transformationMatrix)) }
    GL { glUniformMatrix4fv(projectionMatrixLoc, false, getMatrixBuffer(projectionMatrix)) }
    GL { glUniformMatrix4fv(viewMatrixLoc, false, getMatrixBuffer(viewMatrix)) }
    // loadVec3(lightPosLoc, light.position)
    // loadVec3(lightColLoc, light.color)
  }

  override def draw2D(
      cameraPos: Matrix4f,
      spriteTransform: Matrix4f,
      spriteOffset: SpriteOffset
  ): Unit = ???
}
