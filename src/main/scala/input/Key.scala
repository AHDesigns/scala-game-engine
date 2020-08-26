package input

sealed trait KeyPress

case class KeyUp() extends KeyPress

case class KeyDown() extends KeyPress

case class KeyHold() extends KeyPress
