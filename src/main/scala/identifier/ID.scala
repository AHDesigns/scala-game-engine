package identifier

class ID(val id: ID.IdValue) {

  /** compare two ID's by their value, not reference */
  def ==(entity: ID): Boolean = id == entity.id
}

// TODO: probably should make sure there's no id clashes when used in apply
object ID {
  type IdValue = Int
  private var lastId: IdValue = 0

  def apply(id: String): ID = new ID(id.map(_.toInt).sum)

  def apply(id: Int): ID = new ID(id)

  def apply(): ID = {
    lastId += 1
    ID(lastId)
  }
}
