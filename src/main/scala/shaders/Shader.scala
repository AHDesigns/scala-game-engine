package shaders

import components.{Light, Transform}
import loaders.ShaderLoader
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.opengl.GL20._
import utils.Control.{GL, GLU}

trait Shader extends ShaderLoader {
  val program: Int

  protected def loadVec3(location: Int, v: Vector3f): Unit = {
    GL { glUniform3f(location, v.x, v.y, v.z) }
  }

  protected def uniform(name: String)(loader: Int => Unit): Unit = {
    val location = GLU { glGetUniformLocation(program, name) }
    GL { loader(location) }
  }

  protected def uniformTexture(slot: Int = 0, uniformName: String = "textureSampler"): Unit =
    uniform(uniformName) {
      GL { glUniform1i(_, slot) }
    }
}

trait ModelShader extends Shader {
  def draw(
      transformationMatrix: Matrix4f,
      projectionMatrix: Matrix4f,
      viewMatrix: Matrix4f,
      light: Light,
      lTransform: Transform,
      textureId: Option[Int]
  ): Unit
}
