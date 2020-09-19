package components

import eventSystem.{EventListener, InputMove, MouseMove}
import org.joml.Vector3f

class CameraMovement(inverse: Boolean = false) extends EventListener {
  private val speed = 0.1f
  private val rotSpeed = 0.00001f
  private var movement = new Vector3f()
  private var isMoving = false
  private var translation = new Vector3f()

  def init(): Unit = {
    events
      .on[InputMove] {
        case InputMove(0, 0, 0) => isMoving = false
        case InputMove(x, y, _) =>
          movement = new Vector3f(x, 0, y).normalize().mul(speed)
          updateTranslation()
          isMoving = true
      }
      .on[MouseMove] {
        case MouseMove(x, y) =>
//          me.transform.rotation.y -= (x * rotSpeed)
//          me.transform.rotation.x += (y * rotSpeed * (if (inverse) -1 else 1))
          updateTranslation()
      }
  }

  private def updateTranslation(): Unit = {
//    translation = movement
//    translation = movement.rotateY(me.transform.rotation.y, new Vector3f())
  }

  def update(): Unit = {
    if (isMoving) {
//      me.transform.position.add(translation)
    }
  }
}
