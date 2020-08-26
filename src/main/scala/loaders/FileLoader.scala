package loaders

import utils.Control

import scala.io.Source
import scala.util.{Failure, Success, Try}

class FileLoader(private val dir: String, private val stripNewlines: Boolean = false) {
  def load(file: String): Either[String, String] = {
    val filePath = dir + "/" + file + ".shader"

    readText(filePath, stripNewlines) match {
      case Failure(exception) => Left(s"Could not read file at $filePath\n$exception")
      case Success(value) => Right(value)
    }
  }

  private def readText(file: String, stripNewline: Boolean): Try[String] = {
    Try {
      val lines = Control.using(Source.fromFile(file)) { source =>
        (for (line <- source.getLines()) yield line).mkString(if (stripNewline) "" else "\n")
      }
      lines
    }
  }

}
