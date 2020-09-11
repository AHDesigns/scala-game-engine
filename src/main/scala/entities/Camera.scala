package entities

import org.joml.Vector3f
import utils.Maths.Rot

class Camera
    extends Entity(
      Transform(
        new Vector3f(0, 0, -5),
        Rot(45, 45, 0)
      )
    )
