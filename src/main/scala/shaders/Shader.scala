package shaders

import components.{Light, Transform}
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.opengl.GL20._
import rendy.SpriteOffset
import utils.Control.{GL, GLU}

trait Shader {
  val program: Int

  def draw(
      transformationMatrix: Matrix4f,
      projectionMatrix: Matrix4f,
      viewMatrix: Matrix4f,
      light: Light,
      lTransform: Transform,
      textureId: Option[Int]
  ): Unit

  def draw2D(cameraPos: Matrix4f, spriteTransform: Matrix4f, spriteOffset: SpriteOffset): Unit

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
