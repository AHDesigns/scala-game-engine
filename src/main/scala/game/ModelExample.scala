package game

import components._
import entities.{CoordinateSystem, Entity}
import org.joml.Vector3f
import systems.System
//import systems.physics.ColliderSystem
//import systems.{MoveSystem, PlayerMovementSystem}
import utils.Maths.Rot

object ModelExample extends App with BambooEngine {
  override val systems: Seq[System] = List()

  gameLoop {
    new Entity("singletons")
      .addComponent(CameraActive(Some("player camera")))

//    val iso = Transform(new Vector3f(2, 2, 2), Rot(45, 45, 0))
    val iso = Transform(new Vector3f(), Rot())
    new Entity("camera")
      .addComponent(iso)
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))

    new Entity("Main light")
      .addComponent(Transform(position = new Vector3f(0, 0, 25)))
      .addComponent(Light(new Vector3f(1, 1, 1)))

    new CoordinateSystem(10, loader)
  }
}
