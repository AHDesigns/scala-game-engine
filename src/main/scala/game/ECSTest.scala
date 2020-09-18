package game

import ecs._

object ECSTest extends App {
  // reference MoveSystem to activate
  MoveSystem.init()

  val player = new Entity().addComponent(Transform(1, 1))

  val player2 = new Entity()

  player.hasComponent[Transform]
  player2
    .addComponent(Model("", 0f))
    .addComponent(Transform(2, 2))
    .hasComponent[Model]

  println(ECS.getComponents[Transform])
  MoveSystem.update()
  MoveSystem.addMsg(MoveEvent(player.id, 10, 10))
  MoveSystem.update()
  println(ECS.getComponents[Transform])
  MoveSystem.addMsg(MoveEvent(player2.id, 10, 10))
  MoveSystem.update()
  println(ECS.getComponents[Transform])
}
