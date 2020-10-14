package game

import scala.util.Random

object QuadMaker {
  case class Mesh(vertices: Array[Float], indices: Array[Int])
  def makeQuads(count: Int): Mesh = {
    // pos 3
    // col 3
    // uv 2
    // texId 1
    // vertex length = 9
    // vertex count = 4
    // quad total = 9 * 4 = 36
    val vertices = new Array[Float](36 * count)

    // position             colour          uv         textureId
    def makeQuad = {
      val x = Random.nextFloat() * 2
      val y = Random.nextFloat() * 2
      val texId = Random.between(0, 2).toFloat
      val base = 0.03f
      Array(
        -0.97f + x,
        -0.97f + y,
        0.0f, /* */ 1,
        0,
        0, /* */ 0,
        1, /* */ texId, // top right
        -0.97f + x,
        -1f + y,
        0.0f, /*    */ 0,
        1,
        0, /* */ 1,
        1, /* */ texId, // bottom right
        -1 + x,
        -1 + y,
        0.0f, /*         */ 0,
        0,
        1, /* */ 1,
        0, /* */ texId, // bottom left
        -1 + x,
        -0.97f + y,
        0.0f, /*     */ 1,
        1,
        1, /* */ 0,
        0, /* */ texId // top left
      )
    }

    val indices = new Array[Int](6 * count)
    val indicesTemplate = Array(
      0, 1, 3, // first
      1, 2, 3 // second
    )

    val step = 36
    for { quad <- 0 until count } {
      val offset = quad * step
      val newQuad = makeQuad
      for { idx <- 0 until step } {
        vertices(idx + offset) = newQuad(idx)
      }

      val iOffset = quad * 4
      val idxOffset = quad * 6
      for { idx <- 0 until 6 } {
        indices(idx + idxOffset) = indicesTemplate(idx) + iOffset
      }
    }

    Mesh(vertices, indices)
  }
}
