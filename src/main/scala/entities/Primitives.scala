package entities

import rendy._

object Primitives {
  val Triangle: MeshData = MeshData(
    PositionsData(
      List(
        0.5f, -0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f
      )
    ),
    List(0, 1, 2),
    colorData = Some(
      ColorData(
        List(
          .5f, .6f, 1f, .1f, .9f, 0f, .9f, .2f, .1f
        )
      )
    )
  )

  val Quad: MeshData = MeshData(
    PositionsData(
      List(
        0.5f, 0.5f, 0.0f, // top right
        0.5f, -0.5f, 0.0f, // bottom right
        -0.5f, -0.5f, 0.0f, // bottom left
        -0.5f, 0.5f, 0.0f // top left
      )
    ),
    List(0, 1, 2, 0, 2, 3),
    textures = Some(TextureData(List(0, 0, 0, 1, 1, 1, 1, 0))),
    normalsData = Some(
      NormalsData(
        List(
          0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1
        )
      )
    )
  )

  val Cube: MeshData = MeshData(
    PositionsData(
      List(
        -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f,
        // Back face
        -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f,
        // Top face
        -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f,
        // Bottom face
        -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f,
        // Right face
        1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f,
        // Left face
        -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f
      )
    ),
    List(
      0, 1, 2, 0, 2, 3, // front
      4, 5, 6, 4, 6, 7, // back
      8, 9, 10, 8, 10, 11, // top
      12, 13, 14, 12, 14, 15, // bottom
      16, 17, 18, 16, 18, 19, // right
      20, 21, 22, 20, 22, 23 // left
    )
  )
}
