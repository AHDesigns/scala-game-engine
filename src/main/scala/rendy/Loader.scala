package rendy

import java.nio.{FloatBuffer, IntBuffer}

import game.GameObject
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30._

import scala.collection.mutable.ListBuffer

class Loader {
  import Helpers._

  var vbos = new ListBuffer[Int]()
  var vaos = new ListBuffer[Int]()

  def loadToVAO(data: List[Float]): RawModel = {
    val vaoID = createVAO()
    loadToVBO(data)
    glVertexAttribPointer(0, 3, GL_FLOAT, false, 0,0)
    //    unbindVAO()
    glBindBuffer(GL_ARRAY_BUFFER, 0)
    glBindVertexArray(0)
    RawModel(vaoID)
  }

  private def loadToVBO(data: List[Float]): Unit = {
    val vboID = createVBO()
    glBindBuffer(GL_ARRAY_BUFFER, vboID)
    glBufferData(GL_ARRAY_BUFFER, storeDataInBuffer(data), GL_STATIC_DRAW)
  }

  private def createVAO(): Int = {
    val vaoID = glGenVertexArrays()
    vaos += vaoID
    glBindVertexArray(vaoID)
    vaoID
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
}