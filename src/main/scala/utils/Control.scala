package utils

import org.lwjgl.opengl.GL11._

import scala.annotation.tailrec
import scala.language.reflectiveCalls

object Control {
  def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
    try {
      f(resource)
    } finally {
      resource.close()
    }

  def GL[T](fn: => T): T = {
//    GL_NO_ERROR
    clearErrors
    val res = fn
    val err = glGetError()
    if (err != 0) {
      val errCode = err match {
        case GL_INVALID_ENUM      => "INVALID_ENUM"
        case GL_INVALID_VALUE     => "INVALID_VALUE"
        case GL_INVALID_OPERATION => "INVALID_OPERATION"
        case GL_STACK_OVERFLOW    => "STACK_OVERFLOW"
        case GL_STACK_UNDERFLOW   => "STACK_UNDERFLOW"
        case GL_OUT_OF_MEMORY     => "OUT_OF_MEMORY"
        case _                    => "UNKNOWN"
      }
      new RuntimeException(s"GL error $err | $errCode").printStackTrace()
      java.lang.System.exit(1)
    }
    res
  }

  def GLU(fn: => Int): Int = {
    val uniform = GL { fn }
    if (uniform == -1) {
      new RuntimeException("could not get uniform").printStackTrace()
      java.lang.System.exit(1)
    }
    uniform
  }

  @tailrec
  private def clearErrors: Int = if (glGetError() == 0) 0 else clearErrors
}
