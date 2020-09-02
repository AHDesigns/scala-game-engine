package shaders

import entities.Light
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.opengl.GL20._
import utils.Control.GL

trait Shader {
  val program: Int

  def draw(
            transformationMatrix: Matrix4f,
            projectionMatrix: Matrix4f,
            viewMatrix: Matrix4f,
            light: Light
          ): Unit

  protected def loadVec3(location: Int, v: Vector3f): Unit = {
    GL(glUniform3f(location, v.x, v.y, v.z))
  }
}
