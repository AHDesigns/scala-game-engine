package utils

import org.joml.Vector3f

sealed class Direction(val x: Float = 0f, val y: Float = 0f, val z: Float = 0f) {
  def toVec = new Vector3f(x, y, z)
}

object Directions {
  case object None extends Direction()
  case object Right extends Direction(1f)
  case object Left extends Direction(-1f)
  case object Up extends Direction(0, 1f)
  case object Down extends Direction(0, -1f)
  case object Forward extends Direction(0, 0, -1f)
  case object Backward extends Direction(0, 0, 1f)
}
