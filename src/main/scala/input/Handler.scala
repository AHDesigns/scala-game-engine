package input

import eventSystem._
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import window.Window

class Handler(window: Window) extends Events {
  def poll(): Unit = glfwPollEvents()

  // Setup a key callback. It will be called every time a key is pressed, repeated or released.
  glfwSetKeyCallback(window.id, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
    log(key, action)
    if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) EventSystem ! WindowClose()
  })
  glfwSetWindowCloseCallback(window.id, _ => {
    EventSystem ! WindowClose()
  })

  events.on[GameLoopTick](_ => {
    // Poll for window events. The key callback will only be invoked during this call.
    //      glfwPollEvents()
  }).on[GameEnd](_ => {
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(window.id)
    events.unsubscribe()
  })

  private def log(key: Int, action: Int): Unit = {
    println("key press: " + key + " | action: " + action)
  }
}
