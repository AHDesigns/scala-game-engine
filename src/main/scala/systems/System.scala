package systems

import identifier.Identifier

trait System extends Identifier {
  def init(): Unit
  def update(): Unit
}
