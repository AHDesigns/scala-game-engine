package object eventSystem {
  type ID = Int

  object EventID {
    var lastId = 0;
    def gen(): ID = {
      lastId += 1
      lastId
    }
  }
}
