package utils

import java.nio.{FloatBuffer, IntBuffer}

import org.lwjgl.BufferUtils

trait BufferUtils {
  def storeBuffer(data: List[Float]): FloatBuffer = {
    val buffer = BufferUtils.createFloatBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }

  def storeBuffer(data: List[Int]): IntBuffer = {
    val buffer = BufferUtils.createIntBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }
}
