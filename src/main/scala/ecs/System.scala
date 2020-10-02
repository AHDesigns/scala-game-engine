package ecs

trait System extends ComponentObserver {
  def init(): Unit
  def update(timeElapsed: Float): Unit
}
