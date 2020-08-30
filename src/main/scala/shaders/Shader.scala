package shaders

import org.joml.Matrix4f

trait Shader {
  val program: Int

  def draw(transformationMatrix: Matrix4f, projectionMatrix: Matrix4f): Unit
}
