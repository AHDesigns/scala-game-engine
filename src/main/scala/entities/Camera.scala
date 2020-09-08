package entities

import eventSystem._
import org.joml.Vector3f
import utils.Maths.Rotation

class Camera
    extends Entity(
      position = new Vector3f(0, 0, -5),
      //      rotation = new Rotation(30, 30, 0)
      rotation = new Rotation(45, 45, 0)
    )
    with Events {
  //  this.transform.reflect()
}
