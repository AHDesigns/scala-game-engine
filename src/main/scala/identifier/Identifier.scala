package identifier

trait Identifier {
  val id: IdWrapper = IdManager.gen()
}
