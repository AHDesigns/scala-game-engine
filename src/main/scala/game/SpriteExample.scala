package game

import components._
import entities.Entity
import loaders.{FontLoader, SpriteLoader}
import org.joml.Vector3f
import systems.{CamaraSystem, System}
//import systems.physics.ColliderSystem
//import systems.{MoveSystem, PlayerMovementSystem}
import utils.Maths.Rot

object SpriteExample extends App with BambooEngine {
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
//    val spriteMap = Map(
//      "floor" -> (1, 1),
//      "door" -> (8, 3),
//      "thing" -> (22, 21),
//      "random" -> (22, 31)
//    )
    val spriteMap = (for {
      column <- 1 to 4
      rows = 32
      row <- 1 to rows
    } yield {
      val index = ((column - 1) * rows) + row
      index.toString -> (row, column)
    }).toMap
    val spriteSheet = spriteLoader.loadSpriteSheet("monsters", "sprites.bmp", 16, spriteMap)

    // 116 rows
    // 64 cols
    val maxId = 32 * 4
    println("starting map creation")
    for {
      x <- 0 until (3824 / 2) by 16
      y <- 0 until (2054 / 2) by 16
    } {
      val id = {
        val random = (Math.random() * maxId).toInt
        if (random == 0) 1 else random
      }
      new Entity()
        .addComponent(Transform(new Vector3f(x.toFloat, y.toFloat, 0)))
        .addComponent(Sprite(id.toString, spriteSheet))
    }
    println("finished map")
    //
//    new Entity()
//      .addComponent(Transform(new Vector3f(0, 16, 0)))
//      .addComponent(Sprite("2", spriteSheet))
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
