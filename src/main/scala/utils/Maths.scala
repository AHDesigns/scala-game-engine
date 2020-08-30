package utils

import org.joml.{Matrix4f, Vector3f}
import org.lwjgl.system.MemoryStack

object Maths {
  def thingy(): Unit = {
    try {
      val stack = MemoryStack.stackPush
      try {
        val fb = new Matrix4f()
          .perspective(Math.toRadians(45.0f).toFloat, 1.0f, 0.01f, 100.0f)
          .lookAt(0.0f, 0.0f, 10.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f)
          .get(stack.mallocFloat(16))
        //      glUniformMatrix4fv(mat4Location, false, fb)
      } finally if (stack != null) stack.close()
    }

  }

  def createTransformationMatrix(translation: Vector3f, rotation: Rotation, scale: Float): Matrix4f = {
    new Matrix4f()
      .translate(translation)
      .rotate(Math.toRadians(rotation.x).toFloat, new Vector3f(1, 0, 0))
      .rotate(Math.toRadians(rotation.y).toFloat, new Vector3f(0, 1, 0))
      .rotate(Math.toRadians(rotation.z).toFloat, new Vector3f(0, 0, 1))
      .scale(new Vector3f(scale, scale, scale))
  }

  class Rotation(var x: Float, var y: Float, var z: Float) {
    def +=(rotation: Rotation): Rotation = {
      x += rotation.x
      y += rotation.y
      z += rotation.z
      this
    }
  }

}
