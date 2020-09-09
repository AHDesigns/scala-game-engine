package identifier

object IdManager {
  private var lastId: ID = 0

  def gen(): IdWrapper = {
    lastId += 1
    new IdWrapper(lastId)
  }
}
