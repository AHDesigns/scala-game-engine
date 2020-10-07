package game

import components._
import entities.{Entity, Primitives}
import org.joml.Vector3f
import shaders.TextureShader
import systems.System
//import systems.physics.ColliderSystem
//import systems.{MoveSystem, PlayerMovementSystem}
import utils.Maths.Rot

object GameLogic extends App with BambooEngine {
  override val FPS: Int = 60

  override val systems: Seq[System] = List(
//    PlayerMovementSystem,
//    MoveSystem,
  )

  gameLoop {
    new Entity("singletons")
      .addComponent(CameraActive(Some("player camera")))

    val iso = Transform(new Vector3f(2, 2, 2), Rot(45, 45, 0))
    val square = Transform(new Vector3f(0, 0, 2))
    new Entity("camera")
      .addComponent(square)
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))

    new Entity("Main light")
      .addComponent(Transform(position = new Vector3f(0, 0, 25)))
      .addComponent(Light(new Vector3f(1, 1, 1)))
    //    .addComponent(PlayerMovement(isCamera = true))

    new Entity()
      .addComponent(Transform(new Vector3f(1), rotation = Rot(0, 180), scale = 0.5f))
      .addComponent(
        Model(
          loader.loadPrimitive(Primitives.Quad, Some(loader.loadTexture("flumpy.jpg"))),
          new TextureShader("")
        )
      )

    new Entity()
      .addComponent(Transform(new Vector3f(), rotation = Rot(0, 0)))
      .addComponent(Sprite("1"))

//    new Entity()
//      .addComponent(Transform(new Vector3f(), scale = 0.5f))
//      .addComponent(
//        Model(
//          loader.loadTexturedModel("primitive/cube_smooth", "flumpy.jpg"),
//          new TextureShader("")
//        )
//      )

//    new CoordinateSystem(10, loader)
  }
}
