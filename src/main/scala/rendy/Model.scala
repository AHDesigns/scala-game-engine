package rendy

case class ModelData(
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
  println("steps", attributes.map(_.step).sum)
}

case class TexturedModelJ(
    _vertices: Array[Float],
    _indices: Array[Int],
    normals: Array[Float],
    textures: Array[Float]
) {
  val vertices: List[Float] = _vertices.toList
  val indices: List[Int] = _indices.toList
}

sealed trait Model

sealed case class BasicModel(vaoID: Int, indicesSize: Int, attributes: Seq[Int])
    extends Model

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