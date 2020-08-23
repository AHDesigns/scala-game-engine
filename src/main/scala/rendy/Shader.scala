package rendy

import org.lwjgl.opengl.{GL11, GL20}
import org.lwjgl.opengl.GL20._

class Shader {
  val vertexShaderProgram = """#version 330 core
      |layout (location = 0) in vec3 aPos;
      |
      |void main()
      |{
      |    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
      |}
      |""".stripMargin

  val fragmentShaderProgram =
    """#version 330 core
      |out vec4 FragColor;
      |
      |void main()
      |{
      |    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
      |}
      |""".stripMargin

  val vertexShader = compileShader(vertexShaderProgram, GL_VERTEX_SHADER)
  val fragmentShader = compileShader(fragmentShaderProgram, GL_FRAGMENT_SHADER)
  val shaderProgram = linkShaders(List(vertexShader,fragmentShader))


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
