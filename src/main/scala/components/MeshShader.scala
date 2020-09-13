package components

import rendy.Mesh
import shaders.Shader
import systems.RenderSystem

case class MeshShader(mesh: Mesh, shader: Shader) extends Component {
  override def init(): Unit = {
    RenderSystem.models += ((me.id, this))
  }

  override def destroy(): Unit = {
    RenderSystem.models -= me.id
  }

  def getTransform: Option[Transform] = me.getComponent(Transform.getClass)
}
