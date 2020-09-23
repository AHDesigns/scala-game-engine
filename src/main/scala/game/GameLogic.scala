package game

import ecs.colliders.{Plane, Sphere}
import ecs._
import entities.CoordinateSystem
import org.joml.{Vector3f, Vector4f}
import shaders.StaticShader
import systems.physics.RigidBodySystem
import systems.{MoveSystem, PlayerMovementSystem}
import utils.Axis
import utils.Maths.Rot

object GameLogic extends App with BambooEngine {
  override val FPS: Int = 60

  override val systems: Seq[ecs.System] = List(
    RigidBodySystem,
    PlayerMovementSystem,
    MoveSystem
  )

  gameLoop {
    new Entity("camera")
      .addComponent(Transform(new Vector3f(10, 0, 10), Rot(0, 45, 0)))
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))
      .addComponent(Collider(Sphere(1)))
    //    .addComponent(PlayerMovement(true))
    //    .addComponent(Transform(new Vector3f(0, 5, 0), Rot(45, 45)))

    new Entity("Floor")
      .addComponent(Collider(Plane(Axis.Y, -10f)))

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
      .addComponent(RigidBody())
    //    .addComponent(PlayerMovement())

    new CoordinateSystem(10, loader)
  }
}
