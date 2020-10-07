package entities

import components.{Model, Transform}
import loaders.EntityLoader
import org.joml.{Vector3f, Vector4f}
import shaders.StaticShader

import scala.util.Random

class CoordinateSystem(var max: Int, val loader: EntityLoader) {
  private def rand(n: Boolean = false) = Random.nextFloat() + (if (n) -0.5f else 0)

  new Entity()
    .addComponent(Transform(new Vector3f(), scale = 0.5f))
    .addComponent(
      Model(
        loader.loadModel("primitive/cube"),
        new StaticShader(new Vector4f(1, 1, 1, 1f))
      )
    )
  new Entity()
    .addComponent(Transform(new Vector3f(0, 100f, 0), scale = 0.5f))
    .addComponent(
      Model(
        loader.loadModel("primitive/cube"),
        new StaticShader(new Vector4f(1, 1, 1, 1f))
      )
    )

  val entities = -max to max flatMap { idx =>
    {
      List(
        new Entity()
          .addComponent(Transform(new Vector3f(idx.toFloat, 0f, 0f), scale = 0.2f))
          .addComponent(
            Model(
              loader.loadModel("primitive/cube"),
              new StaticShader(new Vector4f(idx.toFloat / max, 1, 1, 1f))
            )
          ),
        new Entity()
          .addComponent(Transform(new Vector3f(0f, idx.toFloat, 0f), scale = 0.2f))
          .addComponent(
            Model(
              loader.loadModel("primitive/cube"),
              new StaticShader(new Vector4f(1, 1, idx.toFloat / max, 1f))
            )
          ),
        new Entity()
          .addComponent(Transform(new Vector3f(0f, 0f, idx.toFloat), scale = 0.2f))
          .addComponent(
            Model(
              loader.loadModel("primitive/cube"),
              new StaticShader(new Vector4f(1, idx.toFloat / max, 1, 1f))
            )
          )
      )
    }
  }
}
