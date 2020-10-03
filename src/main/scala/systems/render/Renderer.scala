package systems.render

import ecs.{Light, Model, Transform}
import eventSystem.DebugWireframe
import org.joml.Matrix4f

trait Renderer {

  /** Initialise any hardware specific settings */
  def setup(): Unit

  /** Render should be responsible for sending the actual draw calls to hardware */
  def render(
      camera: Transform,
      light: Light,
      lTransform: Transform,
      model: Model,
      mTransform: Transform,
      projectionMatrix: Matrix4f
  ): Unit

  /** Toggle wireframe rendering */
  def wireframe(e: DebugWireframe): Unit
}
