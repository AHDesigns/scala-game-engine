package entities

import org.joml.Vector3f

class Light(
    val position: Vector3f = new Vector3f(0, 0, -25),
    val color: Vector3f = new Vector3f(1, 1, 1)
) {}
