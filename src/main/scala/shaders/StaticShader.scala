package shaders

import entities.Light
import loaders.ShaderLoader
import org.joml.{Matrix4f, Vector4f}
import org.lwjgl.opengl.GL20._
import utils.Control.{GL, GLU}
import utils.JavaBufferUtils.getMatrixBuffer

class StaticShader(color: Vector4f) extends Shader with ShaderLoader {
  val program: Int = load("basic") match {
    case Left(err) => println(err); 0
    case Right(id) => id
  }

  def draw(
      transformationMatrix: Matrix4f,
      projectionMatrix: Matrix4f,
      viewMatrix: Matrix4f,
      light: Light
  ): Unit = {
    val colorLocation = GLU {
      glGetUniformLocation(program, "aColor")
    }
    val matrix = GLU {
      glGetUniformLocation(program, "transformationMatrix")
    }
    val projectionMatrixLoc = GLU {
      glGetUniformLocation(program, "projectionMatrix")
    }
    val viewMatrixLoc = GLU {
      glGetUniformLocation(program, "viewMatrix")
    }
    val lightColLoc = GLU {
      glGetUniformLocation(program, "lightCol")
    }
    val lightPosLoc = GLU {
      glGetUniformLocation(program, "lightPos")
    }

    GL {
      glUseProgram(program)
    }
    GL {
      glUniform4f(colorLocation, color.x, color.y, color.z, color.w)
    }
    GL {
      glUniformMatrix4fv(matrix, false, getMatrixBuffer(transformationMatrix))
    }
    GL {
      glUniformMatrix4fv(projectionMatrixLoc, false, getMatrixBuffer(projectionMatrix))
    }
    GL {
      glUniformMatrix4fv(viewMatrixLoc, false, getMatrixBuffer(viewMatrix))
    }
    loadVec3(lightPosLoc, light.position)
    loadVec3(lightColLoc, light.color)
  }
}