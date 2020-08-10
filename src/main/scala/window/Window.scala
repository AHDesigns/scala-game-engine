package window

import game.GameObject
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW._
import org.lwjgl.opengl.GL11.{GL_COLOR_BUFFER_BIT, GL_DEPTH_BUFFER_BIT, glClear}
import org.lwjgl.system.MemoryStack._
import org.lwjgl.system.MemoryUtil._

class Window extends Runnable with GameObject {
  glfwDefaultWindowHints() // optional, the current window hints are already the default
  glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE) // the window will stay hidden after creation
  glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE) // the window will be resizable

  // Create the window
  val id: Long = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL)
  if (id == NULL) throw new RuntimeException("Failed to create the GLFW window")

  // Make the OpenGL context current
  glfwMakeContextCurrent(id)
  // Enable v-sync
  glfwSwapInterval(1)
  // Make the window visible
  glfwShowWindow(id)

  def gameNotStopped(): Boolean = !glfwWindowShouldClose(id)

  override def start(): Unit = {}

  override def run(): Unit = {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT) // clear the framebuffer
    glfwSwapBuffers(id) // swap the color buffers
  }

  override def cleanUp(): Unit = {
    // Free the window callbacks and destroy the window
    glfwFreeCallbacks(id)
    glfwDestroyWindow(id)
  }

  private def center(): Unit = {
    // Get the thread stack and push a new frame
    try {
      val stack = stackPush
      try {
        val pWidth  = stack.mallocInt(1) // int*
        val pHeight  = stack.mallocInt(1)
        // Get the window size passed to glfwCreateWindow
        glfwGetWindowSize(id, pWidth, pHeight)
        // Get the resolution of the primary monitor
        val vidmode  = glfwGetVideoMode(glfwGetPrimaryMonitor)
        // Center the window
        glfwSetWindowPos(id, (vidmode.width - pWidth.get(0)) / 2, (vidmode.height - pHeight.get(0)) / 2)
      } finally if (stack != null) stack.close()
    } // the stack frame is popped automatically
  }
}
