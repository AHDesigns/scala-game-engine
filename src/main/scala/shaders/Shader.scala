package shaders

import behaviours.Light
import entities.Entity
import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.opengl.GL20._
import utils.Control.GL

trait Shader {
  val program: Int

  def draw[A <: Entity with Light](
      transformationMatrix: Matrix4f,
      projectionMatrix: Matrix4f,
      viewMatrix: Matrix4f,
      light: A
  ): Unit

  protected def loadVec3(location: Int, v: Vector3f): Unit = {
    GL { glUniform3f(location, v.x, v.y, v.z) }
  }
}
