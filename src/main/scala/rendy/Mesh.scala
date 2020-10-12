package rendy

case class MeshData(
    positions: PositionsData,
    indices: List[Int],
    textures: Option[TextureData] = None,
    colorData: Option[ColorData] = None,
    normalsData: Option[NormalsData] = None
) {
  val attributes: Seq[AttribData] = List(
    Some(positions),
    colorData,
    textures,
    normalsData
  ).filter(_.isDefined).map(_.get)

  val count: Int = attributes.size
  val stride: Int = attributes.map(_.step).sum
}

sealed trait Mesh

sealed case class BasicMesh(
    vaoID: Int,
    indicesSize: Int,
    attributes: Seq[Int],
    textureId: Option[Int]
) extends Mesh

case class SpriteOffset(x1: Float, x2: Float, y1: Float, y2: Float)
sealed case class SpriteMesh() {}

sealed trait AttribData {
  val vertexData: List[Float]
  val step = 3
}

case class PositionsData(vertexData: List[Float]) extends AttribData

case class NormalsData(vertexData: List[Float]) extends AttribData

case class ColorData(vertexData: List[Float]) extends AttribData

case class TextureData(vertexData: List[Float]) extends AttribData {
  override val step = 2
}
