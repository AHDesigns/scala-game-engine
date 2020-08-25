package rendy

sealed trait Model
final case class RawModel(vaoID: Int, indicesSize: Int) extends Model
