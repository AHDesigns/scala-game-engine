package ecs.colliders

import utils.Axis

sealed trait Colliders

case class Plane(axis: Axis.Val, offset: Float = 0f) extends Colliders
case class Quad(x: Float, y: Float) extends Colliders
case class Sphere(r: Float) extends Colliders
case class Cube(x: Float, y: Float, z: Float) extends Colliders
