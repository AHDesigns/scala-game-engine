package shaders

import loaders.ShaderLoader
import org.lwjgl.opengl.GL20._

class StaticShader(shaderName: String) extends ShaderLoader {
  private var horizon = 0f
  private val shaderProgram = load(shaderName) match {
    case Left(err) => println(err); 0
    case Right(id) => id
  }

  def draw(): Unit = {
    //    val time = glfwGetTime()
    //    val greenValue = (Math.sin(time).toFloat / 2f) + 0.5f
    horizon += 0.001f
    val horizontalLoc = glGetUniformLocation(shaderProgram, "horizontal")
    glUseProgram(shaderProgram)
    glUniform1f(horizontalLoc, horizon)
    //    glUniform4f(vertexColorLocation, greenValue, greenValue, greenValue, 1.0f)
  }
}
