package utils

import scala.io.Source
import scala.util.Try

object Fs {
  def readText(file: String, stripNewline: Boolean = true): Try[String] = {
    Try {
      val lines = Control.using(Source.fromFile(file)) { source =>
        (for (line <- source.getLines()) yield line).mkString(if (stripNewline) "" else "\n")
      }
      lines
    }
  }
}

