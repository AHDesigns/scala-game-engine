package entities

import org.joml.Vector3f
import utils.Maths.Rotation

class Camera
    extends Entity(
      Transform(
        new Vector3f(0, 0, -5),
        new Rotation(45, 45, 0)
      )
    )
