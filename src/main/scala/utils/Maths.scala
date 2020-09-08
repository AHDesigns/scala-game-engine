package utils

import entities.Entity
import org.joml.{Matrix4f, Vector3f}

object Maths {
  private val worldMatrix = new Matrix4f().identity()

  def createTransformationMatrix(entity: Entity): Matrix4f =
    new Matrix4f()
      .translation(entity.position)
      .rotateX(entity.rotation.x)
      .rotateY(entity.rotation.y)
      .rotateZ(entity.rotation.z)
      .scale(entity.scale)

  /**
    * Create Rotation in radians by passing in degrees
    */
  class Rotation(var x: Float = 0, var y: Float = 0, var z: Float = 0) {
    x = x.toRadians
    y = y.toRadians
    z = z.toRadians

    def toVec = new Vector3f(x, y, z)

    def +=(rotation: Rotation): Rotation = {
      x += rotation.x
      y += rotation.y
      z += rotation.z
      this
    }
  }

  val Right = new Vector3f(1f, 0f, 0f)
  val Forward = new Vector3f(0f, 0f, 1f)
  val Up = new Vector3f(0f, 1f, 0f)
}
