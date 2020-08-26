package rendy

import org.lwjgl.opengl.GL20._

class Shader(vertex: String, fragment: String) {
  private val shaderProgram = linkShaders(List(
    compileShader(vertex, GL_VERTEX_SHADER),
    compileShader(fragment, GL_FRAGMENT_SHADER)
  ))

  def compileShader(shaderCode: String, shaderType: Int): Int = {
    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)
    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
      System.out.println(glGetShaderInfoLog(shaderId, 512))
      System.err.println("Could not compile shader!")
      System.exit(-1)
    }

    shaderId
  }

  def linkShaders(shaderIds: List[Int]): Int = {
    val shaderProgram = glCreateProgram()
    shaderIds.foreach(glAttachShader(shaderProgram, _))
    glLinkProgram(shaderProgram)

    if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == 0) {
      System.out.println(glGetProgramInfoLog(shaderProgram, 512))
      System.err.println("Could not link shader!")
      System.exit(-1)
    }

    shaderIds.foreach(glDeleteShader)
    shaderProgram
  }

  def shade(): Unit = {
    glUseProgram(shaderProgram)
  }
}
