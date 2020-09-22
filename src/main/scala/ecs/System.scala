package ecs

import identifier.Identifier

trait System extends Identifier {
  def init(): Unit
  def update(timeElapsed: Double = 0d): Unit
}
