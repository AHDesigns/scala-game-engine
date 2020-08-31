package input

import eventSystem._
import input.Handler.movementKeys
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import window.Window

class Handler(window: Window) extends Events {
  init()

  def poll(): Unit = glfwPollEvents()

  private def init(): Unit = {
    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    glfwSetKeyCallback(window.id, (window: Long, key: Int, scancode: Int, keyAction: Int, mods: Int) => {
      val action = getKeyPress(keyAction)
      //      log(key, action)
      if (movementKeys.contains(key)) move(key, action)
      else if (keyAction == GLFW_RELEASE) {
        if (key == GLFW_KEY_ESCAPE) EventSystem ! WindowClose()
        if (key == GLFW_KEY_X) EventSystem ! DebugWireframe()
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

  private def move(key: Int, action: KeyPress): Unit = {
    action match {
      case KeyDown() | KeyUp() =>
        var x, y, z = 0f
        if (isPressed(GLFW_KEY_A)) x -= 1
        if (isPressed(GLFW_KEY_D)) x += 1
        if (isPressed(GLFW_KEY_W)) y += 1
        if (isPressed(GLFW_KEY_S)) y -= 1
        EventSystem ! InputMove(x, y, z)
      case _ => ;
    }
  }

  private def getKeyPress(action: Int): KeyPress = action match {
    case GLFW_PRESS => KeyDown()
    case GLFW_RELEASE => KeyUp()
    case GLFW_REPEAT => KeyHold()
  }

  private def isPressed(key: Int): Boolean = glfwGetKey(window.id, key) == GLFW_PRESS

  private def log(key: Int, action: KeyPress): Unit = {
    println("key press: " + key + " | action: " + action)
  }
}

object Handler {
  val movementKeys = List(GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D)
}