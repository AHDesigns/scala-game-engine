package eventSystem

trait Identifier {
  val id: IDWrapper = ID.gen()
}
