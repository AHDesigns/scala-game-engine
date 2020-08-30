package loaders

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import rendy.{BasicModel, ModelData}
import utils.Control.GL
import utils.JavaBufferUtils.createBuffer

import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

class EntityLoader extends DaeLoader {
  private val floatSize = 4
  private var vbos = new ListBuffer[Int]()
  private var vaos = new ListBuffer[Int]()

  load("primitive/cube")

  def loadToVAO(modelData: ModelData): BasicModel = {
    val vaoID = glGenVertexArrays()
    vaos += vaoID
    glBindVertexArray(vaoID)

    storeModelData(modelData)
    storeEAO(modelData.indices)

    glBindVertexArray(0)

    BasicModel(vaoID, modelData.indices.size, modelData.attributes)
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
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW)
    // no need to unbind for EAO
  }

  /**
   * Create a Vertex Buffer Object
   *
   * Used for storing VAO data at a specific index
   * size is the number of vertices per position, eg 2D data would be 2, 3D would be 3
   */
  private def storeModelData(modelData: ModelData): Unit = {
    glBindBuffer(GL_ARRAY_BUFFER, createVBO())
    glBufferData(GL_ARRAY_BUFFER, createBuffer(modelData.vertices), GL_STATIC_DRAW)

    for (index <- 0 until modelData.attributes) {
      println(s"binding atrrib $index")
      val size = modelData.size
      val attributes = modelData.attributes
      val stride = (floatSize * size) * attributes

      GL {
        glVertexAttribPointer(index, size, GL_FLOAT, false, stride, if (index == 0) 0 else size)
      }
    }

    glBindBuffer(GL_ARRAY_BUFFER, 0)
  }

  private def createVBO(): Int = {
    val vboID = glGenBuffers()
    vbos += vboID
    vboID
  }
}
