package eventSystem

object ID {
  private var lastId: ID = 0

  def gen(): IDWrapper = {
    lastId += 1
    new IDWrapper(lastId)
  }
}
