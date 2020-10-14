package game

import eventSystem.{EventListener, EventSystem, GameEnd, WindowClose}
import input.Handler
import window.Window

object Batcher extends App with EventListener {
  val FPS = 30
  var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => {
    gameRunning = false
  })

  val renderer = new BatchRenderer()

  var previous = getTime
  var count = 0d
  var average = 0d
  val frameCount = 60

  while (gameRunning) {
    val loopStartTime = getTime
    val elapsed = loopStartTime - previous
    previous = loopStartTime

    if (count < frameCount) {
      average += elapsed
      count += 1
    } else {
      val averageFPS = average / frameCount
      println("FPS: ", (1d / averageFPS).toInt)
      count = 0
      average = 0
    }

    gameLoop(elapsed)
  }

  cleanUp()

  private def getTime = System.nanoTime / 1_000_000_000.0

  private def cleanUp(): Unit = {
    EventSystem ! GameEnd()
    events.unsubscribe()
  }

  private def gameLoop(elapsed: Double): Unit = {
    inputHandler.poll()
    window.draw {
      renderer.draw()
    }
  }
}
