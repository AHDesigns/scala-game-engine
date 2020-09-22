package game

import ecs._
import ecs.colliders.{Plane, Sphere}
import entities.CoordinateSystem
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import org.lwjgl.Version
import shaders.StaticShader
import systems.physics.RigidBodySystem
import systems.render.{GLRenderer, RenderSystem}
import systems.{MoveSystem, PlayerMovementSystem}
import utils.Axis
import utils.Maths.Rot
import window.Window

object Runner extends App with EventListener {
  val FPS = 60
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => gameRunning = false)

  // TODO: register all systems
  RigidBodySystem.init()
  RenderSystem.init(new GLRenderer())
  MoveSystem.init()
  PlayerMovementSystem.init()

  val loader = new EntityLoader()

  new Entity("camera")
    .addComponent(Transform(new Vector3f(10, 0, 10), Rot(0, 45, 0)))
    .addComponent(Camera("player camera"))
    .addComponent(PlayerMovement(isCamera = true))
    .addComponent(Collider(Sphere(1)))
//    .addComponent(PlayerMovement(true))
//    .addComponent(Transform(new Vector3f(0, 5, 0), Rot(45, 45)))

  new Entity("Floor")
    .addComponent(Collider(Plane(Axis.Y, -10f)))

  new Entity("Main light")
    .addComponent(Transform(position = new Vector3f(0, 0, 25)))
    .addComponent(Light(new Vector3f(1, 1, 1)))
//    .addComponent(PlayerMovement(isCamera = true))

  new Entity("Player")
    .addComponent(Transform(scale = 1.5f))
    .addComponent(
      Model(
        loader.loadModel("primitive/cube"),
        new StaticShader(new Vector4f(1, 1, 1, 1))
      )
    )
    .addComponent(RigidBody())
//    .addComponent(PlayerMovement())

  val coordinateSystem = new CoordinateSystem(10, loader)

  val secsPerUpdate = 1d / FPS
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

  var previous = getTime
  while (gameRunning) {
    val loopStartTime = getTime
    val elapsed = loopStartTime - previous
    previous = loopStartTime
    steps += elapsed

    while (steps >= secsPerUpdate) {
      RigidBodySystem.update(elapsed)
      PlayerMovementSystem.update(elapsed)
      MoveSystem.update(elapsed)
      EventSystem ! GameLoopTick()
      inputHandler.poll()
      steps -= secsPerUpdate
    }

    window.draw {
      RenderSystem.update(elapsed)
      // TODO: get in batches rather than an entity at at time
//      Entity.entities.foreach {
//        case (_, entity) => renderer.render(entity, light)
//      }
    }
    sync(loopStartTime.toLong)
  }

  EventSystem ! GameEnd()
  events.unsubscribe()
}
