package ecs

trait System {
  def init(): Unit
  def update(timeElapsed: Float): Unit
}
