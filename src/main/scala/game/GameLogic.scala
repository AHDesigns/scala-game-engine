package game

import ecs._
import entities.CoordinateSystem
import org.joml.{Vector3f, Vector4f}
import shaders.StaticShader
//import systems.physics.ColliderSystem
//import systems.{MoveSystem, PlayerMovementSystem}
import utils.Maths.Rot

object GameLogic extends App with BambooEngine {
  override val FPS: Int = 60

  override val systems: Seq[ecs.System] = List(
//    PlayerMovementSystem,
//    MoveSystem,
  )

  gameLoop {
    new Entity("singletons")
      .addComponent(CameraActive(Some("player camera")))

    new Entity("camera")
      .addComponent(Transform(new Vector3f(2, 0, 2), Rot(0, 45, 0)))
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))

    new Entity("Floor")
      .addComponent(Transform(new Vector3f(0, -1, 0), Rot()))

    new Entity("Main light")
      .addComponent(Transform(position = new Vector3f(0, 0, 25)))
      .addComponent(Light(new Vector3f(1, 1, 1)))
    //    .addComponent(PlayerMovement(isCamera = true))

    new Entity("Player")
      .addComponent(Transform(scale = 1.5f))
      .addComponent(
        Model(
          loader.loadModel("primitive/cube"),
          new StaticShader(new Vector4f(1, 1, 1, 1))
        )
      )
    //    .addComponent(PlayerMovement())

    new CoordinateSystem(10, loader)
  }
}
