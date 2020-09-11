package utils

import entities.{Entity, Transform}
import org.joml.{Matrix4f, Vector3f}

object Maths {
  private val worldMatrix = new Matrix4f().identity()

  def createViewMatrix(entity: Entity): Matrix4f =
    entity match {
      case Entity(Transform(position, rotation, _), _, _, _, _) =>
        new Matrix4f()
          .rotateX(rotation.x)
          .rotateY(rotation.y)
          .rotateZ(rotation.z)
          .translate(-position.x, -position.y, -position.z)
      //          .translate(position)
    }

  def createTransformationMatrix(entity: Entity): Matrix4f =
    entity match {
      case Entity(Transform(position, rotation, scale), _, _, _, _) =>
        new Matrix4f()
          .translation(position)
          .rotateX(rotation.x)
          .rotateY(rotation.y)
          .rotateZ(rotation.z)
          .scale(scale)
    }

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

    def +(rotation: Rotation): Rotation = new Rotation(x, y, z) += rotation
  }

  val Right = new Vector3f(1f, 0f, 0f)
  val Forward = new Vector3f(0f, 0f, 1f)
  val Up = new Vector3f(0f, 1f, 0f)
}
