package game

import behaviours.PlayerMovement
import entities.{Camera, Entity, Light}
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import org.lwjgl.Version
import rendy.Renderer
import shaders.StaticShader
import utils.Maths.Rotation
import window.Window

import scala.util.Random

object Runner extends App with Events {
  val FPS = 60
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => gameRunning = false)

  val camera = new Camera()
  val renderer = new Renderer(camera)
  val light = new Light()
  val loader = new EntityLoader()

  private def rand(n: Boolean = false) =
    Random.nextFloat() + (if (n) -0.5f else 0)

  val entities = (1 to 10 map { _ =>
    new Entity(
      Some(loader.loadModel("primitive/cube")),
      new Vector3f(rand(true) * 10f, rand(true) * 10f, rand(true) * 3f),
      new Rotation(rand() * 360, rand() * 360, 0),
      0.5f,
      Some(new StaticShader(new Vector4f(rand(), rand(), rand(), 1f)))
    )
  }).toList

  val player = new Entity(
    Some(loader.loadModel("primitive/dragon")),
    new Vector3f(),
    new Rotation(),
    0.2f,
    Some(new StaticShader(new Vector4f(1, 1, 1, 1))),
    List(new PlayerMovement())
  )

  val secsPerUpdate = 1d / FPS
  var previous = getTime
  var steps = 0d

  private def sync(loopStartTime: Long): Unit = {
    val loopSlot = 1f / FPS
    val endTime = loopStartTime + loopSlot
    while (getTime < endTime) try {
      Thread.sleep(1)
    } catch {
      case _: InterruptedException => ;
    }
  }

  private def getTime = System.nanoTime / 1_000_000_000.0

  val objects = player :: entities
  while (gameRunning) {
    val loopStartTime = getTime
    val elapsed = loopStartTime - previous
    previous = loopStartTime
    steps += elapsed

    while (steps >= secsPerUpdate) {
      EventSystem ! GameLoopTick()
      steps -= secsPerUpdate
    }

    window.draw {
      objects.foreach(renderer.render(_, light))
    }
    inputHandler.poll()
    sync(loopStartTime.toLong)
  }

  EventSystem ! GameEnd()
  events.unsubscribe()
}
