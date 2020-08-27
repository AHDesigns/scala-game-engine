package shaders

import loaders.FileLoader
import org.lwjgl.glfw.GLFW.glfwGetTime
import org.lwjgl.opengl.GL20._

class Shader(shaderName: String) extends FileLoader("shaders", ".glsl") {
  private val shaderProgram = (for {
    shaderCode <- load(shaderName)
    shaderPair <- splitShader(shaderCode)
    vertexShader <- compileShader(shaderPair.vertex, GL_VERTEX_SHADER)
    fragmentShader <- compileShader(shaderPair.fragment, GL_FRAGMENT_SHADER)
    result <- linkShaders(List(vertexShader, fragmentShader))
  } yield result) match {
    case Left(err) => println(err); 0
    case Right(value) => value
  }

  private def compileShader(shaderCode: String, shaderType: Int): Either[String, Int] = {
    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    glGetShaderi(shaderId, GL_COMPILE_STATUS) match {
      case 0 => Left(s"Could not compile shader! ${glGetShaderInfoLog(shaderId, 512)}")
      case _ => Right(shaderId)
    }
  }

  private def linkShaders(shaderIds: List[Int]): Either[String, Int] = {
    val shaderProgram = glCreateProgram()
    shaderIds.foreach(glAttachShader(shaderProgram, _))
    glLinkProgram(shaderProgram)
    shaderIds.foreach(glDeleteShader)

    glGetProgrami(shaderProgram, GL_LINK_STATUS) match {
      case 0 => Left(s"Could not link shader!\n${glGetProgramInfoLog(shaderProgram, 512)}")
      case _ => Right(shaderProgram)
    }
  }

  private def splitShader(shaderCode: String): Either[String, ShaderPair] = {
    val shaders = shaderCode.split("#---")
    shaders.length match {
      case 2 => Right(ShaderPair(vertex = shaders(0), fragment = shaders(1)))
      case _ => Left(
        s"""shader '$shaderName' did not appear to contain a vertex and fragment section.
           |Does the file contain a '#---' line to indicate the separation""".stripMargin
      )
    }
  }

  def shade(): Unit = {
    val time = glfwGetTime()
    val greenValue = (Math.sin(time).toFloat / 2f) + 0.5f
    val vertexColorLocation = glGetUniformLocation(shaderProgram, "ourColor")
    glUseProgram(shaderProgram)
    //    glUniform4f(vertexColorLocation, 0.0f, greenValue, 0.0f, 1.0f)
    glUniform4f(vertexColorLocation, greenValue, greenValue, greenValue, 1.0f)
  }
}
