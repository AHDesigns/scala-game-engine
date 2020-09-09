package loaders

import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._
import rendy._
import utils.Control.GL
import utils.JavaBufferUtils.createBuffer

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.language.postfixOps

class EntityLoader extends ObjLoader {
  private var vbos = new ListBuffer[Int]()
  private var vaos = new ListBuffer[Int]()
  private val cache = mutable.Map.empty[String, Model]

  def loadModel(filePath: String): Model = {
    cache.getOrElseUpdate(filePath, loadPrimitive(load(filePath)))
  }

  def loadPrimitive(modelData: ModelData): BasicModel = {
    val vaoID = GL {
      glGenVertexArrays()
    }
    vaos += vaoID
    GL {
      glBindVertexArray(vaoID)
    }

    storeEAO(modelData.indices)
    val attributes = modelData.attributes.map {
      storeAttrib
    }

    GL {
      glBindVertexArray(0)
    }

    BasicModel(vaoID, modelData.indices.size, attributes)
  }

  private def storeAttrib(data: AttribData): Int = {
    GL {
      glBindBuffer(GL_ARRAY_BUFFER, createVBO())
    }

    val attribId = data match {
      case _: PositionsData => 0
      case _: NormalsData   => 1
      case _: ColorData     => 2
      case _: TextureData   => 3
    }
    bindVertex(data, attribId)

    GL {
      glBindBuffer(GL_ARRAY_BUFFER, 0)
    }
    attribId
  }

  private def bindVertex(data: AttribData, index: Int): Unit = {
    GL {
      glBufferData(GL_ARRAY_BUFFER, createBuffer(data.vertexData), GL_STATIC_DRAW)
    }
    GL {
      glVertexAttribPointer(index, data.step, GL_FLOAT, false, 0, 0)
    }
  }

  /**
    * Create Element Array Object
    *
    * Used for storing a models indices
    * EAO's are automatically bound to the currently bound VAO so call this before unbinding
    * the VAO
    */
  private def storeEAO(indices: List[Int]): Unit = {
    GL {
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, createVBO())
    }
    GL {
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, createBuffer(indices), GL_STATIC_DRAW)
    }
    // no need to unbind for EAO
  }

  private def createVBO(): Int = {
    val vboID = GL {
      glGenBuffers()
    }
    vbos += vboID
    vboID
  }
}
