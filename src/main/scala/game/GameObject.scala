package game

trait GameObject {
  def start(): Unit
  def run(): Unit
  def cleanUp(): Unit
}
