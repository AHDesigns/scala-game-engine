package input

import eventSystem._
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import window.Window

class Handler(window: Window) extends Events {
  init()

  def poll(): Unit = glfwPollEvents()

  private def init(): Unit = {
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window.id, (window: Long, key: Int, scancode: Int, action: Int, mods: Int) => {
      log(key, action)
      if (action == GLFW_RELEASE) {
        if (key == GLFW_KEY_ESCAPE) EventSystem ! WindowClose()
        if (key == GLFW_KEY_W) EventSystem ! DebugWireframe()
      }
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
  }

  private def getKeyPress(action: Int): KeyPress = action match {
    case GLFW_PRESS => KeyDown()
    case GLFW_RELEASE => KeyUp()
    case GLFW_REPEAT => KeyHold()
  }

  private def log(key: Int, action: Int): Unit = {
    println("key press: " + key + " | action: " + action)
  }
}
