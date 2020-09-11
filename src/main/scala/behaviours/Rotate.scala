package behaviours

class Rotate extends Behaviour {
  override def update(): Unit = {
    me.transform.rotation.y += 0.1f
  }
}
