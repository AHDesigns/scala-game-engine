package loaders

import logging.Logger
import rendy.SpriteOffset

import scala.util.{Failure, Success, Try}
import scala.util.matching.Regex

case class Glyph(
    spriteImage: SpriteImage,
    xOffset: Int,
    yOffset: Int,
    xAdvance: Int,
    char: Char
)

case class CharInfo(char: Char, glyph: Glyph)
case class Kerning(first: Char, second: Char, amount: Int)

class FontLoader(fontPath: String, fontInfoPath: String)
    extends FileLoader
    with TextureLoader
    with Logger {
  val fontTexture: Texture = loadTexture(fontPath)

  def glyphs(text: String): IndexedSeq[Glyph] = text map { getChar }

  def getKerning(first: Char, second: Char): Int = {
    kernings.getOrElse(first -> second, 0)
  }

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

  private val kernings: Map[(Char, Char), Int] = {
    readFileByLines("res/" + fontInfoPath) { source =>
      (for {
        line <- source.getLines()
        kerning <- getKerning(line)
      } yield kerning).toMap
    } match {
      case Left(err)    => throw new RuntimeException(s"failed $err")
      case Right(value) => value
    }
  }

  private def getKerning(line: String): Option[((Char, Char), Int)] = {
    val kerningRegex: Regex =
      """#first=(\d+) #second=(\d+) #amount=([-?\d]+)""".r
    if (!line.startsWith("#kerning ")) {
      None
    } else {
      Try {
        (for (patternMatch <- kerningRegex.findAllMatchIn(line))
          yield {
            implicit val pat: Regex.Match = patternMatch
            g(1).toInt.toChar -> g(2).toInt.toChar -> g(3).toInt
          }).toList.headOption
      } match {
        case Failure(exception) =>
          logWarn(exception.toString)
          None
        case Success(None) =>
          logWarn(s"Could not parse line: [$line] to Kerning")
          None
        case Success(value) => value
        case _              => throw new RuntimeException("inexhaustive match")
      }
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
          yield {
            implicit val pat: Regex.Match = patternMatch
            g(1).toInt.toChar -> Glyph(
              SpriteImage(
                fontTexture,
                SpriteOffset(
                  x1 = g(2).toFloat,
                  x2 = g(4).toFloat,
                  y1 = g(3).toFloat,
                  y2 = g(5).toFloat
                ),
                size = Some(g(6).toFloat -> g(7).toFloat)
              ),
              xOffset = g(8).toInt,
              yOffset = g(9).toInt,
              xAdvance = g(10).toInt,
              char = g(1).toInt.toChar
            )
          }).toList.headOption
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

  private def g(i: Int)(implicit patternMatch: Regex.Match) = patternMatch.group(i)
}
