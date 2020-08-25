package window

import eventSystem.{Events, GameEnd}
import org.lwjgl.glfw.GLFW._
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11._
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._
import utils.System.isMacOs

class Window extends Events {
  // Create the window
  private val n = GLFW_FALSE
  private val y = GLFW_TRUE

  // Setup an error callback. The default implementation
  // will print the error message in System.err.
  GLFWErrorCallback.createPrint(System.err).set

  if (! glfwInit() ) {
    println("failed to initialise GLFW")
    System.exit(1)
  }

  glfwDefaultWindowHints() // optional, the current window hints are already the default
  glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
  glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
  glfwWindowHint(GLFW_VISIBLE, n) // the window will stay hidden after creation
  glfwWindowHint(GLFW_RESIZABLE, y) // the window will be resizable
  glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

  if (isMacOs) {
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, y)
  }

  val id: Long = glfwCreateWindow(800, 600, "Hello World!", NULL, NULL)
  if (id == NULL) throw new RuntimeException("Failed to create the GLFW window")

  // Make the OpenGL context current
  glfwMakeContextCurrent(id)
  // Enable v-sync
  glfwSwapInterval(1)
  // Make the window visible
  glfwShowWindow(id)
  // does all the things
  GL.createCapabilities
  println(glGetString(GL_VERSION))

  events
    .on[GameEnd](_ => {
      cleanUp()
    })
  // .on[GameLoopTick](_ => { update() })

  def clean(): Unit = {
    glClearColor(0.3f, 0.2f, 0.8f, 0.0f)
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer
  }

  def draw(): Unit = {
    glfwSwapBuffers(id) // swap the color buffers
  }

  private def cleanUp(): Unit = {
    //  Destroy the window
    glfwDestroyWindow(id)

    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
    events.unsubscribe()
  }

  private def center(): Unit = {
    // Get the thread stack and push a new frame
    val stack = stackPush
    try {
      val pWidth = stack.mallocInt(1) // int*
      val pHeight = stack.mallocInt(1)
      // Get the window size passed to glfwCreateWindow
      glfwGetWindowSize(id, pWidth, pHeight)
      // Get the resolution of the primary monitor
      val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor)
      // Center the window
      glfwSetWindowPos(id, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
    } finally if (stack != null) stack.close()
  } // the stack frame is popped automatically
}
