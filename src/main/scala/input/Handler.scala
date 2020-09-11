package input

import eventSystem._
import input.Handler.movementKeys
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import utils.Control.GL
import window.Window

class Handler(window: Window) extends EventListener {
  val mouseSensitivity = 70 // 1 - 100 would be in-game option
  private var lastMouse: (Int, Int) = window.getMousePos match {
    case (x, y) => (x * mouseSensitivity, y * mouseSensitivity)
  }
  init()

  def poll(): Unit = glfwPollEvents()

  private def init(): Unit = {
    // hide the mouse
    GL {
      glfwSetInputMode(window.id, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
    }

    // Setup a key callback. It will be called every time a key is pressed, repeated or released.
    GL {
      glfwSetKeyCallback(
        window.id,
        (window: Long, key: Int, scancode: Int, keyAction: Int, mods: Int) => {
          val action = getKeyPress(keyAction)
          //      log(key, action)
          if (movementKeys.contains(key)) move(key, action)
          else if (keyAction == GLFW_RELEASE) {
            if (key == GLFW_KEY_ESCAPE) EventSystem ! WindowClose()
            if (key == GLFW_KEY_X) EventSystem ! DebugWireframe()
          }
        }
      )
    }

    GL {
      glfwSetCursorPosCallback(
        window.id,
        (_, x: Double, y: Double) => {
          // 0,0 is top left
          val (oldX, oldY) = lastMouse
          // round to Int as these come in as really precise doubles, but the decimal value never changes (Macbook pro)
          val newX = x.toInt * mouseSensitivity
          val newY = y.toInt * mouseSensitivity
          EventSystem ! MouseMove(newX - oldX, newY - oldY)
          lastMouse = (newX, newY)
        }
      )
    }

    GL {
      glfwSetWindowCloseCallback(window.id, _ => EventSystem ! WindowClose())
    }

    events.on[GameEnd](_ => {
      // Free the window callbacks and destroy the window
      GL {
        glfwFreeCallbacks(window.id)
      }
      events.unsubscribe()
    })
  }

  private def move(key: Int, action: KeyPress): Unit = {
    action match {
      case KeyDown() | KeyUp() =>
        var x, y, z = 0f
        if (isPressed(GLFW_KEY_A)) x -= 1
        if (isPressed(GLFW_KEY_D)) x += 1
        if (isPressed(GLFW_KEY_W)) y -= 1
        if (isPressed(GLFW_KEY_S)) y += 1
        EventSystem ! InputMove(x, y, z)
      case _ => ;
    }
  }

  private def getKeyPress(action: Int): KeyPress =
    action match {
      case GLFW_PRESS   => KeyDown()
      case GLFW_RELEASE => KeyUp()
      case GLFW_REPEAT  => KeyHold()
    }

  private def isPressed(key: Int): Boolean =
    glfwGetKey(window.id, key) == GLFW_PRESS

  private def log(key: Int, action: KeyPress): Unit = {
    println("key press: " + key + " | action: " + action)
  }
}

object Handler {
  val movementKeys = List(GLFW_KEY_W, GLFW_KEY_A, GLFW_KEY_S, GLFW_KEY_D)
}
