package entities

import behaviours.Behaviour
import eventSystem.{EventListener, GameLoopTick}
import org.joml.Vector3f
import rendy.Model
import shaders.Shader
import utils.Maths.Rotation

class Entity(
    val model: Option[Model] = None,
    val position: Vector3f = new Vector3f(0, 0, 0),
    val rotation: Rotation = new Rotation(0, 0, 0),
    val scale: Float = 1f,
    val shader: Option[Shader] = None,
    val behaviours: List[Behaviour] = List.empty
) extends EventListener {
  this.behaviours foreach (_.init(this))
  this.events.on[GameLoopTick] { _ => this.behaviours foreach (_.update()) }
}

object Entity {
  val empty = new Entity(
    None,
    new Vector3f(0, 0, 0),
    new Rotation(0, 0, 0),
    0.0f
  )
}
