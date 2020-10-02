package identifier

import identifier.ID.IdValue

trait Identifier {
  val identifier: ID = ID()
  val id: IdValue = identifier.id
}
