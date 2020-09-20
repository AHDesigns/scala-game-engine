package systems.render

import org.joml.Matrix4f

trait Renderer {

  /** Initialise any hardware specific settings */
  def setup(): Unit

  /** Render should be responsible for sending the actual draw calls to hardware */
  def render(
      renderMeshShader: RenderModel,
      light: RenderLight,
      camera: RenderCamera,
      projectionMatrix: Matrix4f
  ): Unit

  /** Toggle wireframe rendering */
  def wireframe(): Unit
}
