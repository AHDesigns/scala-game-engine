package game

import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.lwjgl.Version
import rendy.Renderer
import shaders.StaticShader
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => {
    gameRunning = false
  })

  val loader = new EntityLoader()

  //  val vertices = List(
  //    0.5f, 0.5f, 0.0f, // top right
  //    0.5f, -0.5f, 0.0f, // bottom right
  //    -0.5f, -0.5f, 0.0f, // bottom left
  //    -0.5f, 0.5f, 0.0f // top left
  //  )
  val vertices = List(
    // positions         // colors
    0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, // bottom right
    -0.5f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, // bottom left
    0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f // top
  )

  val indices = List(
    0, 1, 2
  )

  val model = loader.loadToVAO(vertices, indices)
  val shader = new StaticShader("basic")
  val renderer = new Renderer()

  while (gameRunning) {
    window.clean()
    shader.draw()
    renderer.render(model)

    EventSystem ! GameLoopTick()

    window.draw()
    inputHandler.poll()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
