package input

import org.lwjgl.glfw.GLFW._
import window.Window
import game.GameObject

class Handler(window: Window) extends GameObject {
  // Setup a key callback. It will be called every time a key is pressed, repeated or released.
  glfwSetKeyCallback(window.id, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
    log(key, action)
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) glfwSetWindowShouldClose(window, true)
  })

  override def start(): Unit = {}

  override def cleanUp(): Unit = {}

  override def run(): Unit = {
    // Poll for window events. The key callback will only be invoked during this call.
    glfwPollEvents()
  }

  private def log(key: Int, action: Int): Unit = {
    println("key press: " + key + " | action: " + action)
  }
}
