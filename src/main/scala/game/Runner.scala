package game

import eventSystem.{EventSystem, Events, GameEnd, GameLoopTick, WindowClose}
import input.Handler
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.{glfwInit, glfwSetErrorCallback, glfwTerminate}
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.glClearColor
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  // Setup an error callback. The default implementation
  // will print the error message in System.err.
  GLFWErrorCallback.createPrint(System.err).set

  if (! glfwInit() ) {
    println("failed to initialise GLFW")
    System.exit(1)
  }

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on(WindowClose.id, _ => { gameRunning = false })

  GL.createCapabilities
  glClearColor(0.3f, 0.2f, 0.8f, 0.0f)

  while (gameRunning) {
    EventSystem ! GameLoopTick()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
  // Terminate GLFW and free the error callback
  glfwTerminate()
  glfwSetErrorCallback(null).free()
}
