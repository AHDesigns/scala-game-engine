package window

import eventSystem.{EventListener, EventSystem, GameEnd, WindowResize}
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryUtil._
import utils.Control.GL
import utils.System.isMacOs

class Window extends EventListener {
  // Create the window
  private val n = GLFW_FALSE
  private val y = GLFW_TRUE

  // Setup an error callback. The default implementation
  // will print the error message in System.err.
  GLFWErrorCallback.createPrint(System.err).set

  if (!glfwInit()) {
    println("failed to initialise GLFW")
    System.exit(1)
  }

  //  glfwDefaultWindowHints() // optional, the current window hints are already the default
  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
  glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
  glfwWindowHint(GLFW_VISIBLE, n) // the window will stay hidden after creation
  glfwWindowHint(GLFW_RESIZABLE, y) // the window will be resizable

  if (isMacOs) {
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, y)
  }

  val id: Long = glfwCreateWindow(1000, 600, "GamyMcGameFace", NULL, NULL)
  if (id == NULL) throw new RuntimeException("Failed to create the GLFW window")

  // Make the OpenGL context current
  glfwMakeContextCurrent(id)
  // Enable v-sync
  glfwSwapInterval(1)
  // Make the window visible
  glfwShowWindow(id)
  // does all the things
  createCapabilities

  println(glGetString(GL_VERSION))
  GL {
    glfwSetFramebufferSizeCallback(
      id,
      (_, width, height) => {
        EventSystem ! WindowResize(width, height)
        GL { glViewport(0, 0, width, height) }
      }
    )
  }

  events.on[GameEnd] { cleanUp }

  def draw(drawFn: => Unit): Unit = {
    GL { glClearColor(0.0f, 0.8f, 0.8f, 0.0f) }
    // clear the framebuffer
    GL { glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) }
    drawFn
    // swap the color buffers
    GL { glfwSwapBuffers(id) }
  }

  def getMousePos: (Int, Int) = {
    import org.lwjgl.BufferUtils
    val x = BufferUtils.createDoubleBuffer(1)
    val y = BufferUtils.createDoubleBuffer(1)
    GL { glfwGetCursorPos(id, x, y) }
    (x.get().toInt, y.get().toInt)
  }

  private def cleanUp(e: GameEnd): Unit = {
    //  Destroy the window
    glfwDestroyWindow(id)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
    events.unsubscribe()
  }
}
