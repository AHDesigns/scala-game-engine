package utils

import org.lwjgl.opengl.GL11.glGetError

import scala.annotation.tailrec
import scala.language.reflectiveCalls

object Control {
  def using[A <: {def close(): Unit}, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def GL[T](fn: => T): T = {
    clearErrors
    val res = fn
    val err = glGetError()
    if (err != 0) {
      new RuntimeException(s"GL error $err").printStackTrace()
      java.lang.System.exit(1)
    }
    res
  }

  @tailrec
  private def clearErrors: Int = if (glGetError() == 0) 0 else clearErrors
}
