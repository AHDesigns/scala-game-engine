package loaders

import utils.Control

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}

trait FileLoader {
  protected def loadFile(file: String, stripNewlines: Boolean = false): Either[String, String] = {
    readText(file, stripNewlines) match {
      case Failure(exception) => Left(s"Could not read file at $file\n$exception")
      case Success(value)     => Right(value)
    }
  }

  protected def readFileByLines[T](
      file: String
  )(lineProcessor: BufferedSource => T): Either[String, T] = {
    Try {
      Control.using(Source.fromFile(file)) { lineProcessor }
    } match {
      case Success(value) => Right(value)
      case Failure(exception) =>
        exception.printStackTrace()
        Left(s"Could not read file at $file\n$exception")
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
