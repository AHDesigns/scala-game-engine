package loaders

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import rendy.BasicModel
import utils.BufferUtils

import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

class EntityLoader extends DaeLoader with BufferUtils {
  private val floatSize = 4
  private var vbos = new ListBuffer[Int]()
  private var vaos = new ListBuffer[Int]()

  load("primitive/cube")

  def loadToVAO(vertices: List[Float], indices: List[Int]): BasicModel = {
    val vaoID = glGenVertexArrays()
    vaos += vaoID
    glBindVertexArray(vaoID)

    storeModelData(vertices, 3)
    storeEAO(indices)

    glBindVertexArray(0)

    BasicModel(vaoID, indices.size)
  }

  /**
   * Create Element Array Object
   *
   * Used for storing a models indices
   * EAO's are automatically bound to the currently bound VAO so call this before unbinding
   * the VAO
   */
  private def storeEAO(indices: List[Int]): Unit = {
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, createVBO())
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeBuffer(indices), GL_STATIC_DRAW)
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
    glBufferData(GL_ARRAY_BUFFER, storeBuffer(data), GL_STATIC_DRAW)
    glVertexAttribPointer(0, size, GL_FLOAT, false, (floatSize * size) * 2, 0)
    glVertexAttribPointer(1, size, GL_FLOAT, false, (floatSize * size) * 2, floatSize * size)
    glBindBuffer(GL_ARRAY_BUFFER, 0)
  }

  private def createVBO(): Int = {
    val vboID = glGenBuffers()
    vbos += vboID
    vboID
  }
}
