package game

import entities.{Entity, Primitives}
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.Vector3f
import org.lwjgl.Version
import rendy.Renderer
import shaders.ColorShader
import utils.Maths.Rotation
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

  val model = loader.loadToVAO(Primitives.Cube)
  val shader = new ColorShader("color")
  val entity = new Entity(model, new Vector3f(0, 0, 0), new Rotation(0, 0, 0), 1, shader)
  val renderer = new Renderer()

  while (gameRunning) {
    window.clean()
    renderer.render(entity)
    //    entity.increaseRotation(new Rotation(0,0,1))
    entity.increasePosition(new Vector3f(0, 0, -0.01f))

    EventSystem ! GameLoopTick()

    window.draw()
    inputHandler.poll()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
