package systems.render

import ecs._
import eventSystem._
import identifier.ID
import org.joml.Matrix4f

case class RenderModel(model: Model, transform: Transform)
case class RenderLight(light: Light, transform: Transform)
case class RenderCamera(camera: Camera, transform: Transform)

object RenderSystem extends EventListener {
  private var renderer: Renderer = _

  private var projectionMatrix = perspective(800, 400)
  private var activeCamera: ID = ID("camera")
  // private var viewMatrix = Maths.createViewMatrix(camera)

  // TODO: quick first pass
  var cameras = Map.empty[ID, RenderCamera]
  var lights = List.empty[RenderLight]
  var models = List.empty[RenderModel]

  def update(): Unit = {
    // TODO: make active camera rather than just doing this
    val camera = cameras.getOrElse(activeCamera, throw new RuntimeException("no active camera"))
    val light = lights.find(_.isInstanceOf[RenderLight]).get

    models foreach { model => renderer.render(model, light, camera, projectionMatrix) }
  }

  def init(renderer: Renderer): Unit = {
    this.renderer = renderer
    renderer.setup()

    events
      .on[WindowResize] { window => projectionMatrix = perspective(window.width, window.height) }
      .on[DebugWireframe] { _ => renderer.wireframe() }
      .on[GameEnd] { _ => events.unsubscribe() }
      .on[ComponentTransformCreated] {
        case ComponentTransformCreated(_, entity) => storeComponent(entity)
      }
      .on[ComponentModelCreated] { case ComponentModelCreated(_, entity) => storeComponent(entity) }
      .on[ComponentCameraCreated] {
        case ComponentCameraCreated(_, entity) => storeComponent(entity)
      }
      .on[ComponentLightCreated] { case ComponentLightCreated(_, entity) => storeComponent(entity) }
    //    .on[CameraMove] { camera => viewMatrix = camera.transform }
  }

  private def storeComponent(entity: Entity): Unit = {
    (
      entity.getComponent[Transform],
      entity.getComponent[Camera],
      entity.getComponent[Model],
      entity.getComponent[Light]
    ) match {
      case (Some(transform :: Nil), Some(camera :: Nil), None, None) =>
        cameras = cameras + (entity.id -> RenderCamera(camera, transform))
      case (Some(transform :: Nil), None, Some(model :: Nil), None) =>
        models = RenderModel(model, transform) :: models
      case (Some(transform :: Nil), None, None, Some(light :: Nil)) =>
        lights = RenderLight(light, transform) :: lights
      case _ => ;
    }
  }

  private def perspective(w: Int, h: Int) =
    new Matrix4f().setPerspective(
      Math.toRadians(70).toFloat,
      w.toFloat / h.toFloat,
      0.01f,
      1000f
    )
}
