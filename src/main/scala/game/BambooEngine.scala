package game

import ecs._
import eventSystem._
import input.Handler
import loaders.EntityLoader
import org.lwjgl.Version
import systems.RenderSystem
import systems.render.GLRenderer
import window.Window

trait BambooEngine extends EventListener {
  val FPS: Int
  val loader = new EntityLoader()

  val systems: Seq[System]

  def gameLoop(gameSetup: => Unit): Unit = {
    println("Using LWJGL " + Version.getVersion + "!")
    var gameRunning = true

    val window = new Window()
    val inputHandler = new Handler(window)

    events.on[WindowClose](_ => {
      gameRunning = false
    })
    // Setup all systems
    val renderer = new RenderSystem(new GLRenderer())
    (renderer +: systems) foreach (_.init())

    gameSetup

    val secsPerUpdate = 1d / FPS
    var steps = 0d

    var previous = getTime
    while (gameRunning) {
      val loopStartTime = getTime
      val elapsed = loopStartTime - previous
      previous = loopStartTime
      steps += elapsed

      while (steps >= secsPerUpdate) {
        // Update all systems
        systems foreach (_.update(elapsed.toFloat))
        // Broadcast game loop event, not sure if I need this
        EventSystem ! GameLoopTick(elapsed.toFloat)
        // Get any inputs from the user ready for the next frame.
        inputHandler.poll()
        steps -= secsPerUpdate
      }

      window.draw {
        renderer.update(elapsed.toFloat)
      }
      // sleep the thread based on the target FPS
      sync(loopStartTime.toLong)
    }

    cleanUp()
  }

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

  private def cleanUp(): Unit = {
    EventSystem ! GameEnd()
    events.unsubscribe()
  }
}
