package game

import eventSystem._
import input.Handler
import org.lwjgl.Version
import rendy.{Loader, Renderer, Shader}
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => { gameRunning = false })

  val loader = new Loader()

  val vertices = List(
    0.5f, 0.5f, 0.0f, // top right
    0.5f, -0.5f, 0.0f, // bottom right
    -0.5f, -0.5f, 0.0f, // bottom left
    -0.5f, 0.5f, 0.0f // top left
  )

  val indices = List(
    0, 1, 3,
    1, 2, 3
  )

  val model = loader.loadToVAO(vertices, indices)
  val shader = new Shader()
  val renderer = new Renderer()

  while (gameRunning) {
    window.clean()
    shader.shade()
    renderer.render(model)

    EventSystem ! GameLoopTick()

    window.draw()
    inputHandler.poll()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
