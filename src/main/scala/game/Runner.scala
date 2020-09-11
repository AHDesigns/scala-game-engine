package game

import behaviours.CameraMovement
import entities.{Entity, Light, Transform}
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import org.lwjgl.Version
import rendy.Renderer
import shaders.StaticShader
import window.Window

import scala.util.Random

object Runner extends App with EventListener {
  val FPS = 60
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => gameRunning = false)

  val camera = new Entity(
    //    Transform(
    //      new Vector3f(0, 5, 0),
    //      new Rotation(45, 45, 0)
    //    ),
    behaviours = List(
      new CameraMovement()
      //      new Follow(
      //        ID("Player"),
      //        Transform(
      //          new Vector3f(0, 5, -5),
      //          new Rotation(45, 45, 0)
      //        )
      //      )
    )
  )
  val renderer = new Renderer(camera)
  val light = new Light()
  val loader = new EntityLoader()

  private def rand(n: Boolean = false) = Random.nextFloat() + (if (n) -0.5f else 0)

  1 to 100 foreach { _ =>
    new Entity(
      Transform(
        new Vector3f(rand(true) * 50f, 0f, rand(true) * 50f)
      ),
      Some(loader.loadModel("primitive/cube")),
      Some(new StaticShader(new Vector4f(rand(), rand(), rand(), 1f)))
    )
  }

  // Player
  new Entity(
    Transform(scale = 1.5f),
    Some(loader.loadModel("primitive/cube")),
    Some(new StaticShader(new Vector4f(1, 1, 1, 1))),
    //    List(new PlayerMovement()),
    name = "Player"
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
      // TODO: get in batches rather than an entity at at time
      Entity.entities.foreach {
        case (_, entity) => renderer.render(entity, light)
      }
    }
    inputHandler.poll()
    sync(loopStartTime.toLong)
  }

  EventSystem ! GameEnd()
  events.unsubscribe()
}
