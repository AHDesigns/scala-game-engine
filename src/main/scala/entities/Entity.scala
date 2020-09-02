package entities

import org.joml.Vector3f
import rendy.Model
import shaders.Shader
import utils.Maths.Rotation

class Entity(
    val model: Model,
    var position: Vector3f,
    var rotation: Rotation,
    var scale: Float,
    val shader: Shader
) {
  def increasePosition(pos: Vector3f): Unit = {
    this.position.add(pos)
  }

  def increaseRotation(rotation: Rotation): Unit = {
    this.rotation += rotation
  }

}
