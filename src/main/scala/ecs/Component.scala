package ecs

import identifier.Uuid

trait Component extends Uuid {
  // ------------------------------------------------
  //              METHODS                           -
  // ------------------------------------------------
  val singleton = false
  def componentActiveInSystem: Boolean = _componentActiveInSystem
  def deactivate(): Unit = _componentActiveInSystem = false

  def entityId: Int = _entityId

  def entityId_=(entityId: Int): Unit = if (_entityId == 0) _entityId = entityId

  def getSibling[A <: Component](implicit componentId: ComponentId[A]): Option[A] = {
    for {
      entity <- entityManager.getEntity(entityId)
      sibling <- entity.getSibling[A](componentId.id)
    } yield sibling
  }
  // ------------------------------------------------
  //              PRIVATES                          -
  // ------------------------------------------------
  private val entityManager = EntityManager
  private var _componentActiveInSystem: Boolean = true
  private var _entityId: Int = 0
}
