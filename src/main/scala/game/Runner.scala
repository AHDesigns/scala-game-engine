package game

import eventSystem.{EventSystem, Events, GameEnd, GameLoopTick, WindowClose}
import input.Handler
import org.lwjgl.Version
import org.lwjgl.glfw.GLFW.{glfwInit, glfwSetErrorCallback, glfwTerminate}
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.{GL_TRIANGLES, glClearColor, glDrawArrays}
import org.lwjgl.opengl.GL20.{glDisableVertexAttribArray, glEnableVertexAttribArray}
import org.lwjgl.opengl.GL30.glBindVertexArray
import rendy.{Loader, Shader}
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  // Setup an error callback. The default implementation
  // will print the error message in System.err.
  GLFWErrorCallback.createPrint(System.err).set

  if (! glfwInit() ) {
    println("failed to initialise GLFW")
    System.exit(1)
  }

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => { gameRunning = false })

  GL.createCapabilities
  glClearColor(0.3f, 0.2f, 0.8f, 0.0f)

  val loader = new Loader()

  val vertices = List(
    -0.5f, -0.5f, 0.0f,
    0.5f, -0.5f, 0.0f,
    0.0f,  0.5f, 0.0f
  )

  val model = loader.loadToVAO(vertices)
  val shader = new Shader()

  while (gameRunning) {
    shader.shade()
    glBindVertexArray(model.vaoID)

    glEnableVertexAttribArray(0)
    glDrawArrays(GL_TRIANGLES, 0, 3)
    glDisableVertexAttribArray(0)
    glBindVertexArray(0)
    EventSystem ! GameLoopTick()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
