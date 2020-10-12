package game

import components._
import entities.Entity
import loaders.FontLoader
import org.joml.Vector3f
import systems.System

object TextExample extends App with BambooEngine {
  override val systems: Seq[System] = List()

  gameLoop {
    new Entity("singletons")
      .addComponent(CameraActive(Some("player camera")))

    new Entity("camera")
      .addComponent(Transform(new Vector3f(0, 0, 0)))
      .addComponent(Camera("player camera"))
      .addComponent(PlayerMovement(isCamera = true))

    implicit val font: FontLoader = new FontLoader("font/font1bmp.png", "font/font1bmp.fnt")

    new Entity()
      .addComponent(Text("hello game!"))
      .addComponent(Transform())

    new Entity()
      .addComponent(
        Text(
          "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean eu fringilla nisi. Vestibulum elementum pellentesque mi sollicitudin laoreet. Nunc sodales elementum risus, interdum ultrices metus volutpat ut. Sed sollicitudin massa vitae venenatis pretium. Curabitur quis ante et est interdum luctus et scelerisque elit. Pellentesque nibh diam, viverra ac urna non, euismod suscipit ligula. Etiam a erat ut lorem sagittis sodales. Aliquam a lacus metus. Fusce non nunc posuere, scelerisque arcu eget, facilisis massa. Maecenas nisl arcu, lacinia sit amet purus eget, consectetur euismod lorem. Morbi mi ipsum, posuere a vestibulum in, elementum eu tellus. Sed condimentum in magna ut viverra. Cras eu facilisis nisl. Maecenas nec bibendum sem, tincidunt aliquet lorem. In porttitor at urna congue interdum.\n\nDonec id nunc luctus, mollis neque in, elementum augue. Pellentesque blandit, diam sit amet tempor dictum, odio sem varius lorem, fermentum vulputate eros mi eu lectus. Cras luctus nisi vitae viverra dictum. Sed vel rhoncus urna. Nulla quis dui interdum, vulputate tellus sed, gravida libero. Morbi commodo vel velit nec dapibus. Sed sodales tellus in sapien vehicula, et accumsan tortor iaculis. Vivamus pretium est est, id tincidunt odio interdum ac. Quisque aliquam ipsum quis elit maximus, sit amet blandit velit venenatis. Interdum et malesuada fames ac ante ipsum primis in faucibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin mattis nulla nulla, ac placerat nibh aliquam a. Sed eget orci nisl."
        )
      )
      .addComponent(Transform(new Vector3f(0, 300, 0)))
//      .addComponent(Transform(new Vector3f(950, 750, 0)))
  }
}
