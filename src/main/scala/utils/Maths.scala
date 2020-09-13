package utils

import components.Transform
import org.joml.{Matrix4f, Vector3f}

object Maths {
  def createViewMatrix(camera: Transform): Matrix4f =
    camera match {
      case Transform(position, rotation, _) =>
        new Matrix4f()
          .rotateX(rotation.x)
          .rotateY(-rotation.y)
          .rotateZ(rotation.z)
          .translate(new Vector3f(position).negate())
    }

  def createTransformationMatrix(modelTransform: Transform): Matrix4f =
    modelTransform match {
      case Transform(position, rotation, scale) =>
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
  case class Rot(var x: Float = 0, var y: Float = 0, var z: Float = 0) {
    x = x.toRadians
    y = y.toRadians
    z = z.toRadians

    def toVec = new Vector3f(x, y, z)

    def +=(rotation: Rot): Rot = {
      x += rotation.x
      y += rotation.y
      z += rotation.z
      this
    }

    def +(rotation: Rot): Rot = Rot(x, y, z) += rotation
  }

  val Right = new Vector3f(1f, 0f, 0f)
  val Forward = new Vector3f(0f, 0f, 1f)
  val Up = new Vector3f(0f, 1f, 0f)
}
