package loaders

import java.nio.{FloatBuffer, IntBuffer}

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import rendy.RawModel

import scala.collection.mutable.ListBuffer

class Loader {

  import Helpers._

  var vbos = new ListBuffer[Int]()
  var vaos = new ListBuffer[Int]()

  def loadToVAO(vertices: List[Float], indicies: List[Int]): RawModel = {
    val vaoID = glGenVertexArrays()
    vaos += vaoID
    glBindVertexArray(vaoID)

    storeModelData(vertices, 3)
    storeEAO(indicies)

    glBindVertexArray(0)

    RawModel(vaoID, indicies.size)
  }

  /**
   * Create Element Array Object
   *
   * Used for storing a models indicies
   * EAO's are automatically bound to the currently bound VAO so call this before unbinding
   * the VAO
   */
  private def storeEAO(indices: List[Int]): Unit = {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, createVBO())
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeDataInBuffer(indices), GL_STATIC_DRAW)
    // no need to unbind for EAO
  }

  /**
   * Create a Vertex Buffer Object
   *
   * Used for storing VAO data at a specific index
   * size is the number of vertices per position, eg 2D data would be 2, 3D would be 3
   */
  private def storeModelData(data: List[Float], size: Int): Unit = {
    glBindBuffer(GL_ARRAY_BUFFER, createVBO())
    glBufferData(GL_ARRAY_BUFFER, storeDataInBuffer(data), GL_STATIC_DRAW)
    glVertexAttribPointer(0, size, GL_FLOAT, false, floatSize * size, 0)
    glBindBuffer(GL_ARRAY_BUFFER, 0)
  }

  private def createVBO(): Int = {
    val vboID = glGenBuffers()
    vbos += vboID
    vboID
  }
}

object Helpers {
  def storeDataInBuffer(data: List[Float]): FloatBuffer = {
    val buffer = BufferUtils.createFloatBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }

  def storeDataInBuffer(data: List[Int]): IntBuffer = {
    val buffer = BufferUtils.createIntBuffer(data.length).put(data.toArray)
    buffer.flip()
    buffer
  }

  val floatSize = 4
}