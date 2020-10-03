package identifier

import identifier.ID.IdValue

trait Identifier {
  val identifier: ID = ID()
  def id: IdValue = identifier.id
}
