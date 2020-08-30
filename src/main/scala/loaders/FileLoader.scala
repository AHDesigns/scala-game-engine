package loaders

import utils.Control

import scala.io.Source
import scala.util.{Failure, Success, Try}

trait FileLoader {
  protected def load(file: String, stripNewlines: Boolean = false): Either[String, String] = {
    readText(file, stripNewlines) match {
      case Failure(exception) => Left(s"Could not read file at $file\n$exception")
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
