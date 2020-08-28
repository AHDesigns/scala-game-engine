package rendy

class ModelData(val vertices: List[Float], val indices: List[Int]) {}

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

sealed case class BasicModel(vaoID: Int, indicesSize: Int) extends Model
