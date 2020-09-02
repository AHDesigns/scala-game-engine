package game

import entities.{Camera, Entity, Light, Primitives}
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import org.lwjgl.Version
import rendy.Renderer
import shaders.{ColorShader, StaticShader}
import utils.Maths.Rotation
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => gameRunning = false)

  val renderer = new Renderer()
  val camera = new Camera(0.1f)
  val light = new Light()
  val loader = new EntityLoader()

  val entity = new Entity(
    loader.loadPrimitive(Primitives.Triangle),
    new Vector3f(0, 0, -2.5f),
    new Rotation(0, 0, 0),
    1,
    new ColorShader("color")
  )

  val entity2 = new Entity(
    loader.loadModel("primitive/cube"),
    new Vector3f(2, 0, -3.5f),
    new Rotation(0, 0, 0),
    0.5f,
    new StaticShader(new Vector4f(.3f, .4f, .8f, 1f))
  )


  while (gameRunning) {
    window.clean()
    List(entity, entity2)
      .foreach(renderer.render(_, light))
    //    renderer.render(entity, light)

    EventSystem ! GameLoopTick()

    window.draw()
    inputHandler.poll()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
