package entities

import org.joml.Vector3f
import utils.Maths.Rotation

case class Transform(
    var position: Vector3f = new Vector3f(0, 0, 0),
    var rotation: Rotation = new Rotation(0, 0, 0),
    var scale: Float = 1f
)
