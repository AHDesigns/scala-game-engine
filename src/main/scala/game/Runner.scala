package game

import components._
import entities.Entity
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import org.lwjgl.Version
import shaders.StaticShader
import systems.{MoveSystem, RenderSystem}
import utils.Maths.Rot
import window.Window

import scala.util.Random

object Runner extends App with EventListener {
  val FPS = 60
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => gameRunning = false)

  // TODO: register all systems
  RenderSystem.init()
  MoveSystem.init()

  val loader = new EntityLoader()

  new Entity()
    .addComponent(Transform(new Vector3f(0, 5, 0), Rot(45, 45, 0)))
    .addComponent(Camera())

  new Entity()
    .addComponent(Transform(position = new Vector3f(0, 0, -25)))
    .addComponent(Light(new Vector3f(1, 1, 1)))

  private def rand(n: Boolean = false) = Random.nextFloat() + (if (n) -0.5f else 0)

//  val max = 100
//  1 to max foreach { idx =>
//    new Entity(
//      Transform(new Vector3f(idx, 0f, 0f), scale = 0.2f),
//      Some(loader.loadModel("primitive/cube")),
//      Some(new StaticShader(new Vector4f(idx / max, 1, 1, 1f)))
//    )
//  }
//  1 to max foreach { idx =>
//    new Entity(
//      Transform(new Vector3f(0f, idx, 0f), scale = 0.2f),
//      Some(loader.loadModel("primitive/cube")),
//      Some(new StaticShader(new Vector4f(1, 1, idx / max, 1f)))
//    )
//  }
//  1 to max foreach { idx =>
//    new Entity(
//      components.Transform(new Vector3f(0f, 0f, idx), scale = 0.2f),
//      Some(loader.loadModel("primitive/cube")),
//      Some(new StaticShader(new Vector4f(1, idx / max, 1, 1f)))
//    )
//  }

  new Entity()
    .addComponent(Transform(scale = 1.5f))
    .addComponent(
      MeshShader(
        loader.loadModel("primitive/cube"),
        new StaticShader(new Vector4f(1, 1, 1, 1))
      )
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
      MoveSystem.update()
      steps -= secsPerUpdate
    }

    window.draw {
      RenderSystem.update()
      // TODO: get in batches rather than an entity at at time
//      Entity.entities.foreach {
//        case (_, entity) => renderer.render(entity, light)
//      }
    }
    inputHandler.poll()
    sync(loopStartTime.toLong)
  }

  EventSystem ! GameEnd()
  events.unsubscribe()
}
