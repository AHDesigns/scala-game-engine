package entities

import eventSystem._
import org.joml.{Matrix4f, Vector3f}

class Camera(speed: Float) extends Events {
  private val position = new Vector3f(0, 0, 0)
  private val pitch = .0f
  private val yaw = .0f
  private val roll = .0f
  private val transform = new Matrix4f().identity()
  private var translation = new Vector3f(0, 0, 0)
  private val direction = new Vector3f(-1, -1, 1)
  private var isMoving = false

  events
    .on[InputMove] {
      case InputMove(0, 0, 0) => isMoving = false
      case InputMove(x, y, z) =>
        translation =
          new Vector3f(x, z, y).normalize().mul(direction).mul(speed)
        isMoving = true
    }
    .on[GameLoopTick] {
      update
    }
    .on[GameEnd] { _ => events.unsubscribe() }

  def getPosition: Vector3f = position

  def getPitch: Float = pitch

  def getYaw: Float = yaw

  def getRoll: Float = roll

  private def update(e: GameLoopTick): Unit = {
    if (isMoving) {
      transform.translate(translation)
      EventSystem ! CameraMove(transform)
    }
  }
}
