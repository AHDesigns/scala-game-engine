package loaders

import logging.Logger

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

case class Glyph(
    char: Int,
    uvStart: (Float, Float),
    uvEnd: (Float, Float),
    size: (Float, Float),
    xOffset: Int,
    yOffset: Int,
    xAdvance: Int
)

class FontLoader(fontPath: String, fontInfoPath: String)
    extends FileLoader
    with TextureLoader
    with Logger {
  val fontTexture: Texture = loadTexture(fontPath)

  def text(text: String): IndexedSeq[Glyph] = text map { getChar }

  private def getChar(c: Char): Glyph = {
    chars.get(c) match {
      case Some(glyph) => glyph
      case None =>
        logWarn(s"missing char info for [$c]")
        chars.getOrElse(
          9072,
          throw new RuntimeException(
            s"missing font info for char: [$c] and fall back char [9072] not in font"
          )
        )
    }
  }

  private val chars: Map[Char, Glyph] = {
    readFileByLines("res/" + fontInfoPath) { source =>
      (for {
        line <- source.getLines()
        charInfo <- processLine(line)
      } yield charInfo).toMap
    } match {
      case Left(err)    => throw new RuntimeException(s"failed $err")
      case Right(value) => value
    }
  }

  private def processLine(line: String): Option[(Char, Glyph)] = {
    val glyphRegex: Regex =
      """#charId=(\d+) #uvStart=\(([\d.]+), ([\d.]+)\) #uvEnd=\(([\d.]+), ([\d.]+)\) #size=\(([\d.]+), ([\d.]+)\) #xOffset=([-?\d.]+) #yOffset=([-?\d.]+) #xAdvance=([-?\d.]+)""".r
    if (!line.startsWith("#charId")) {
      None
    } else {
      Try {
        (for (patternMatch <- glyphRegex.findAllMatchIn(line))
          yield patternMatch.group(1).toInt.toChar -> Glyph(
            char = patternMatch.group(1).toInt,
            uvStart = (patternMatch.group(2).toFloat, patternMatch.group(3).toFloat),
            uvEnd = (patternMatch.group(4).toFloat, patternMatch.group(5).toFloat),
            size = (patternMatch.group(6).toFloat, patternMatch.group(7).toFloat),
            xOffset = patternMatch.group(8).toInt,
            yOffset = patternMatch.group(9).toInt,
            xAdvance = patternMatch.group(10).toInt
          )).toList.headOption
      } match {
        case Failure(exception) =>
          logWarn(exception.toString)
          None
        case Success(None) =>
          logWarn(s"Could not parse line: [$line] to Glyph")
          None
        case Success(value) => value
        case _              => throw new RuntimeException("inexhaustive match")
      }
    }
  }
}
