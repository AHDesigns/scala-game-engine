package game

import input.Handler
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFWErrorCallback
import window.Window

object Runner extends App {
  println("Using LWJGL " + Version.getVersion + "!")

  // Setup an error callback. The default implementation
  // will print the error message in System.err.
  GLFWErrorCallback.createPrint(System.err).set

  if (! glfwInit() ) {
    println("failed to initialise GLFW")
    System.exit(1)
  }

  val window = new Window()
  val inputHandler = new Handler(window)

  new GameLoop(
    window,
    List(inputHandler)
  ).run()
}