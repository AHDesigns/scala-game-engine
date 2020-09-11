package entities

import behaviours.Behaviour
import eventSystem.{EntityCreated, EventListener, EventSystem, GameLoopTick}
import identifier.ID
import rendy.Mesh
import shaders.Shader

import scala.collection.mutable

case class Entity(
    transform: Transform = Transform(),
    mesh: Option[Mesh] = None,
    shader: Option[Shader] = None,
    behaviours: List[Behaviour] = List.empty,
    name: String = ""
) extends EventListener {
  val id: ID = if (name.nonEmpty) ID(name) else ID()
  Entity.entities.addOne(id, this)

  this.behaviours foreach (b => {
    b.bind(this)
    b.init()
  })

  this.events.on[GameLoopTick] { _ =>
    this.behaviours filter (_.hasUpdate) foreach (_.update())
  }

  EventSystem ! EntityCreated(this)

  def destroy(): Unit = {
    Entity.entities.remove(id)
    this.events.unsubscribe()
  }

  /** Get a behaviour if it exists on this entity */
  def getBehaviour(behaviour: Behaviour): Option[Behaviour] =
    behaviours.find(_.getClass == behaviour.getClass)
}

object Entity {
  lazy val empty = new Entity()
  val entities = mutable.HashMap.empty[ID, Entity]
}
