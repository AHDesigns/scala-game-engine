package game

import loaders.{ShaderLoader, TextureLoader}
import logging.Logger
import org.lwjgl.opengl.GL11._
import org.lwjgl.opengl.GL13.{glActiveTexture, GL_TEXTURE0, GL_TEXTURE1}
import org.lwjgl.opengl.GL15._
import org.lwjgl.opengl.GL20._
import org.lwjgl.opengl.GL30.{glBindVertexArray, glGenVertexArrays}
import utils.Control.{GL, GLU}
import utils.JavaBufferUtils.createBuffer

import scala.collection.mutable.ListBuffer
import scala.util.Random

class BatchRenderer extends ShaderLoader with Logger with TextureLoader {
  private var vbos = new ListBuffer[Int]()
  private var vaos = new ListBuffer[Int]()

  val quadCount = 3_000
  // create vao
  val vaoID: Int = GL { glGenVertexArrays() }
  vaos += vaoID
  // bind
  GL { glBindVertexArray(vaoID) }
  val data = QuadMaker.makeQuads(quadCount)
  // -------------------------------------------------------
  // vertices
  // -------------------------------------------------------
  // create vbo
  val vboID: Int = GL { glGenBuffers() }
  vbos += vboID
  GL { glBindBuffer(GL_ARRAY_BUFFER, vboID) }
  // buffer data
  GL { glBufferData(GL_ARRAY_BUFFER, createBuffer(data.vertices), GL_DYNAMIC_DRAW) }
  // create attrib -------------------
  // positions
  val sizeOfFloat = 4
  val stride = (9) * sizeOfFloat
  // positions
  GL { glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0) }
  GL { glEnableVertexAttribArray(0) }
  // color
  GL { glVertexAttribPointer(1, 3, GL_FLOAT, false, stride, (3 * sizeOfFloat)) }
  GL { glEnableVertexAttribArray(1) }
  // uv
  GL { glVertexAttribPointer(2, 2, GL_FLOAT, false, stride, (3 * sizeOfFloat * 2)) }
  GL { glEnableVertexAttribArray(2) }
  // textureId
  GL {
    glVertexAttribPointer(3, 1, GL_FLOAT, false, stride, (3 * sizeOfFloat * 2) + (2 * sizeOfFloat))
  }
  GL { glEnableVertexAttribArray(3) }
  // ---------------------------------
  // unbind buffer
  GL { glBindBuffer(GL_ARRAY_BUFFER, 0) }
  // -------------------------------------------------------
  // indices
  // -------------------------------------------------------
  // create ebo
  val eboID: Int = GL { glGenBuffers() }
  vbos += eboID
  GL { glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID) }
  // buffer data
  GL {
    glBufferData(
      GL_ELEMENT_ARRAY_BUFFER,
      createBuffer(data.indices),
      GL_STATIC_DRAW
    )
  }
  // -------------------------------------------------------
  GL { glBindVertexArray(0) }

  // shader
  val program: Int = load("simple") match {
    case Left(err) => logErr(err); 0
    case Right(id) => id
  }

  GL { glUseProgram(program) }
//  private val colorLocation = GLU { glGetUniformLocation(program, "color") }
//  GL { glUniform4f(colorLocation, 1, 1, 1, 1) }

  GL { glBindVertexArray(vaoID) }

  // bind texture
  val texId = loadTexture("wood.jpeg")
  val texId2 = loadTexture("wood.jpeg")
  GL { glActiveTexture(GL_TEXTURE0) }
  GL { glBindTexture(GL_TEXTURE_2D, texId.textureId) }
  GL { glActiveTexture(GL_TEXTURE1) }
  GL { glBindTexture(GL_TEXTURE_2D, texId2.textureId) }

  GL { glUseProgram(program) }
  val textureLoc = GLU { glGetUniformLocation(program, "textureSampler") }
  GL { glUniform1iv(textureLoc, Array(0, 1)) }

  GL { glBindBuffer(GL_ARRAY_BUFFER, vboID) }
  def draw(): Unit = {
    // buffer data
    val idx = (Random.nextFloat() * quadCount).round * 9
    data.vertices(idx) += 0.1f
    GL {
      glBufferSubData(GL_ARRAY_BUFFER, idx, createBuffer(Array(data.vertices(idx))))
//      glBufferSubData(GL_ARRAY_BUFFER, 0, createBuffer(data.vertices))
    }
    GL { glDrawElements(GL_TRIANGLES, 6 * quadCount, GL_UNSIGNED_INT, 0) }
//    GL { glDrawArrays(GL_TRIANGLES, 0, 3) }
//    GL { glBindVertexArray(0) }
  }

  def cleanup(): Unit = {}
}
