package shaders

import loaders.ShaderLoader
import org.joml.Matrix4f
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL20._
import utils.Control.GL
import utils.JavaBufferUtils.getMatrixBuffer

class ColorShader(shaderName: String) extends Shader with ShaderLoader {
  val program: Int = load(shaderName) match {
    case Left(err) => println(err); 0
    case Right(id) => id
  }
  println(s"shader program $program")

  def draw(transformationMatrix: Matrix4f, projectionMatrix: Matrix4f, viewMatrix: Matrix4f): Unit = {
    val time = glfwGetTime()
    val greenValue = (Math.sin(time).toFloat / 2f) + 0.5f
    val colorLocation = GL(glGetUniformLocation(program, "aColor"))
    val matrix = GL(glGetUniformLocation(program, "transformationMatrix"))
    val projectionMatrixLoc = GL(glGetUniformLocation(program, "projectionMatrix"))
    val viewMatrixLoc = GL(glGetUniformLocation(program, "viewMatrix"))

    if (colorLocation == -1) {
      new RuntimeException("could not get uniform").printStackTrace()
      System.exit(1)
    }
    GL(glUseProgram(program))
    GL(glUniform4f(colorLocation, 0.0f, greenValue, 0.0f, 1.0f))
    GL(glUniformMatrix4fv(matrix, false, getMatrixBuffer(transformationMatrix)))
    GL(glUniformMatrix4fv(projectionMatrixLoc, false, getMatrixBuffer(projectionMatrix)))
    GL(glUniformMatrix4fv(viewMatrixLoc, false, getMatrixBuffer(viewMatrix)))
  }
}
