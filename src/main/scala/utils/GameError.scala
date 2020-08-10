package utils

sealed abstract class GameError
object GameError {
  final case class Window() extends GameError
}
