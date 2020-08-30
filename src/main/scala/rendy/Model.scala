package rendy

case class ModelData(vertices: List[Float], indices: List[Int], size: Int, attributes: Int)

trait Normals {
  protected val normals: List[Float]
}

trait Color {
  protected val color: List[Float]
}

trait Texture {
  protected val texture: List[Float]
}

sealed trait Model

sealed case class BasicModel(vaoID: Int, indicesSize: Int, attributes: Int) extends Model
