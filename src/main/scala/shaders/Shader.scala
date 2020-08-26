package shaders

import loaders.FileLoader
import org.lwjgl.opengl.GL20._

class Shader(vertex: String, fragment: String) extends FileLoader("src/main/scala/shaders/") {
  private val shaderProgram = (for {
    vertexCode <- load(vertex)
    vertexShader <- compileShader(vertexCode, GL_VERTEX_SHADER)
    fragmentCode <- load(fragment)
    fragmentShader <- compileShader(fragmentCode, GL_FRAGMENT_SHADER)
    result <- linkShaders(List(vertexShader, fragmentShader))
  } yield result) match {
    case Left(err) => println(err); 0
    case Right(value) => value
  }

  def compileShader(shaderCode: String, shaderType: Int): Either[String, Int] = {
    val shaderId = glCreateShader(shaderType)
    glShaderSource(shaderId, shaderCode)
    glCompileShader(shaderId)

    glGetShaderi(shaderId, GL_COMPILE_STATUS) match {
      case 0 => Left(s"Could not compile shader! ${glGetShaderInfoLog(shaderId, 512)}")
      case _ => Right(shaderId)
    }
  }

  def linkShaders(shaderIds: List[Int]): Either[String, Int] = {
    val shaderProgram = glCreateProgram()
    shaderIds.foreach(glAttachShader(shaderProgram, _))
    glLinkProgram(shaderProgram)
    shaderIds.foreach(glDeleteShader)

    glGetProgrami(shaderProgram, GL_LINK_STATUS) match {
      case 0 => Left(s"Could not link shader!\n${glGetProgramInfoLog(shaderProgram, 512)}")
      case _ => Right(shaderProgram)
    }
  }

  def shade(): Unit = glUseProgram(shaderProgram)
}
