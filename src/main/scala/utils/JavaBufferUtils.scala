package utils

import java.nio.{FloatBuffer, IntBuffer}

import org.joml.Matrix4f
import org.lwjgl.BufferUtils

object JavaBufferUtils {
  def createBuffer(data: List[Float]): FloatBuffer = {
    val buffer = BufferUtils.createFloatBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }

  def createBuffer(data: List[Int]): IntBuffer = {
    val buffer = BufferUtils.createIntBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }

  def getMatrixBuffer(matrix: Matrix4f, capicity: Int = 16): FloatBuffer = {
    val buffer = BufferUtils.createFloatBuffer(capicity)
    matrix.get(buffer)
    //    buffer.flip()
    buffer
  }
}
