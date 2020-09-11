package behaviours

import org.joml.Vector3f

trait Light extends Behaviour {
  private var color: Vector3f = _

  def setColor(color: Vector3f): Unit = this.color = color
  def getColor: Vector3f = color
}
