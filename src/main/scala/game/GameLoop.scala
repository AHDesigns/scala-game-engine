package game

import org.lwjgl.glfw.GLFW.{glfwSetErrorCallback, glfwTerminate}
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.glClearColor
import window.Window

class GameLoop(window: Window, runnables: List[GameObject]) {
  GL.createCapabilities
  // Set the clear color
  glClearColor(0.3f, 0.2f, 0.8f, 0.0f)

  def run(): Unit = {
    while (window.gameNotStopped()) {
      window.run()
      runnables.foreach(_.run())
    }

    cleanUp()
  }

  private def cleanUp(): Unit = {
    runnables.foreach(_.cleanUp())
    window.cleanUp()
    // Terminate GLFW and free the error callback
    glfwTerminate()
    glfwSetErrorCallback(null).free()
  }
}
