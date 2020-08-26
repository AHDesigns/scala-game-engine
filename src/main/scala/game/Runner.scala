package game

import eventSystem._
import input.Handler
import loaders.Loader
import org.lwjgl.Version
import rendy.{Renderer, Shader}
import window.Window

object Runner extends App with Events {
  println("Using LWJGL " + Version.getVersion + "!")
  private var gameRunning = true

  val window = new Window()
  val inputHandler = new Handler(window)

  events.on[WindowClose](_ => {
    gameRunning = false
  })

  val loader = new Loader()

  val vertices = List(
    0.5f, 0.5f, 0.0f, // top right
    0.5f, -0.5f, 0.0f, // bottom right
    -0.5f, -0.5f, 0.0f, // bottom left
    -0.5f, 0.5f, 0.0f // top left
  )
  val vertices2 = List(
    1.0f, 1.0f, 0.0f, // top right
    1.0f, -1.0f, 0.0f, // bottom right
    0.6f, -1.0f, 0.0f, // bottom left
    0.6f, 1.0f, 0.0f // top left
  )

  val indices = List(
    0, 1, 3,
    1, 2, 3
  )


  val vertexShaderProgram =
    """#version 330 core
      |layout (location = 0) in vec3 aPos;
      |
      |void main()
      |{
      |    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);
      |}
      |""".stripMargin

  val fragmentShaderProgram =
    """#version 330 core
      |out vec4 FragColor;
      |
      |void main()
      |{
      |    FragColor = vec4(0.95f, 0.8f, 0.45f, 1.0f);
      |}
      |""".stripMargin
  val fragmentShaderProgram2 =
    """#version 330 core
      |out vec4 FragColor;
      |
      |void main()
      |{
      |    FragColor = vec4(1.0f, 0.5f, 0.44f, 1.0f);
      |}
      |""".stripMargin

  val model2 = loader.loadToVAO(vertices2, indices)
  val model = loader.loadToVAO(vertices, indices)
  val shader = new Shader(vertex = vertexShaderProgram, fragment = fragmentShaderProgram)
  val shader2 = new Shader(vertex = vertexShaderProgram, fragment = fragmentShaderProgram2)
  val renderer = new Renderer()

  while (gameRunning) {
    window.clean()
    shader.shade()
    renderer.render(model)
    shader2.shade()
    renderer.render(model2)

    EventSystem ! GameLoopTick()

    window.draw()
    inputHandler.poll()
  }

  EventSystem ! GameEnd()

  events.unsubscribe()
}
