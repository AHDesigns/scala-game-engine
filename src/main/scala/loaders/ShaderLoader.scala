package loaders

import org.lwjgl.opengl.GL20._
import utils.Control.GL

trait ShaderLoader extends FileLoader {
  protected def load(shaderFile: String): Either[String, Int] = {
    val shaderPath = "src/main/scala/shaders/" + shaderFile + ".glsl"
    for {
      shaderCode <- super.loadFile(shaderPath)
      shaderPair <- splitShader(shaderCode)
      vertexShader <- compileShader(shaderPair.vertex, GL_VERTEX_SHADER)
      fragmentShader <- compileShader(shaderPair.fragment, GL_FRAGMENT_SHADER)
      shaderProgram <- linkShaders(List(vertexShader, fragmentShader))
    } yield shaderProgram
  }

  private def compileShader(
      shaderCode: String,
      shaderType: Int
  ): Either[String, Int] = {
    val shaderId = GL {
      glCreateShader(shaderType)
    }
    GL {
      glShaderSource(shaderId, shaderCode)
    }
    GL {
      glCompileShader(shaderId)
    }

    glGetShaderi(shaderId, GL_COMPILE_STATUS) match {
      case 0 =>
        Left(s"Could not compile shader! ${glGetShaderInfoLog(shaderId, 512)}")
      case _ => Right(shaderId)
    }
  }

  private def linkShaders(shaderIds: List[Int]): Either[String, Int] = {
    val shaderProgram = GL {
      glCreateProgram()
    }
    shaderIds.foreach(GL {
      glAttachShader(shaderProgram, _)
    })
    GL {
      glLinkProgram(shaderProgram)
    }
    shaderIds.foreach(GL {
      glDeleteShader _
    })

    GL {
      glGetProgrami(shaderProgram, GL_LINK_STATUS)
    } match {
      case 0 => Left(s"Could not link shader!\n${glGetProgramInfoLog(shaderProgram, 512)}")
      case _ => Right(shaderProgram)
    }
  }

  private def splitShader(shaderCode: String): Either[String, ShaderPair] = {
    val shaders = shaderCode.split("#---")
    shaders.length match {
      case 2 => Right(ShaderPair(vertex = shaders(0), fragment = shaders(1)))
      case _ =>
        Left(
          s"""shader did not appear to contain a vertex and fragment section.
            |Does the file contain a '#---' line to indicate the separation""".stripMargin
        )
    }
  }

  case class ShaderPair(vertex: String, fragment: String)

}
