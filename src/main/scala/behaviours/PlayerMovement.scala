package behaviours

import entities.Entity
import eventSystem.{Events, InputMove, MouseMove}
import org.joml.Vector3f

class PlayerMovement extends Behaviour with Events {
  private val speed = 0.1f
  private val rotSpeed = 0.00001f
  private var movement = new Vector3f()
  private var isMoving = false
  private var translation = new Vector3f()

  def init(e: Entity): Unit = {
    me = e
    events
      .on[InputMove] {
        case InputMove(0, 0, 0) => isMoving = false
        case InputMove(x, y, z) =>
          movement = new Vector3f(x, z, y).normalize().mul(speed)
          updateTranslation()
          isMoving = true
      }
      .on[MouseMove] {
        case MouseMove(x, y) =>
          println(x)
          me.rotation.y += (x * rotSpeed)
          updateTranslation()
      }
  }

  private def updateTranslation(): Unit = {
    translation = movement.rotateY(me.rotation.y, new Vector3f())
  }

  override def update(): Unit = {
    if (isMoving) {
      me.position.add(translation)
    }
  }
}
