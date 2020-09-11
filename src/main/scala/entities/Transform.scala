package entities

import org.joml.Vector3f
import utils.Maths.Rot

case class Transform(
    var position: Vector3f = new Vector3f(0, 0, 0),
    var rotation: Rot = Rot(0, 0, 0),
    var scale: Float = 1f
)
