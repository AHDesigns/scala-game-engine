package game

import components._
import entities.Entity
import loaders.{FontLoader, SpriteLoader}
import org.joml.Vector3f
import systems.{CamaraSystem, System}
//import systems.physics.ColliderSystem
//import systems.{MoveSystem, PlayerMovementSystem}
import utils.Maths.Rot

object GameLogic extends App with BambooEngine {
  override val FPS: Int = 60

  override val systems: Seq[System] = List(
    CamaraSystem
//    PlayerMovementSystem,
//    MoveSystem,
  )

  gameLoop {
    new Entity("singletons")
      .addComponent(CameraActive(Some("player camera")))

    val iso = Transform(new Vector3f(2, 2, 2), Rot(45, 45, 0))
//    val square = Transform(new Vector3f(0, 0, 2))
    val square = Transform(new Vector3f(0, 0, 0))
    new Entity("camera")
      .addComponent(square)
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))

    new Entity("Main light")
      .addComponent(Transform(position = new Vector3f(0, 0, 25)))
      .addComponent(Light(new Vector3f(1, 1, 1)))
    //    .addComponent(PlayerMovement(isCamera = true))

//    new Entity()
//      .addComponent(Transform(new Vector3f(1), rotation = Rot(0, 180), scale = 0.5f))
//      .addComponent(
//        Model(
//          loader.loadPrimitive(Primitives.Quad, Some(loader.loadTexture("flumpy.jpg").textureId)),
//          new TextureShader("")
//        )
//      )

    val spriteLoader = new SpriteLoader()
    val spriteMap = Map(
      "floor" -> (1, 1),
      "door" -> (8, 3),
      "thing" -> (22, 21),
      "random" -> (22, 31),
      "fail" -> (220, 31)
    )
    val spriteSheet = spriteLoader.loadSpriteSheet("monsters", "sprites.bmp", 16, spriteMap)

    new Entity()
      .addComponent(Transform(new Vector3f(10, 10, 0)))
      .addComponent(Sprite("door", spriteSheet))
//
//    new Entity()
//      .addComponent(Transform(new Vector3f(1, 0, 0)))
//      .addComponent(Sprite("thing", spriteSheet))
//
//    new Entity()
//      .addComponent(Transform(new Vector3f(0, 1, 0)))
//      .addComponent(Sprite("random", spriteSheet))

//    new Entity()
//      .addComponent(Transform(new Vector3f(0, 1, 0)))
//      .addComponent(Text("random"))

    implicit val font: FontLoader = new FontLoader("font/font1bmp.png", "font/font1bmp.fnt")

    new Entity()
      .addComponent(Text("he"))
      .addComponent(Transform())

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
