package loaders

import logging.Logger
import rendy.SpriteOffset

import scala.collection.mutable

case class SpriteImage(texture: Texture, spriteOffset: SpriteOffset)
class SpriteSheet(
    spriteSheetId: String,
    val spriteSize: Int,
    spriteMap: SpriteMap,
    val texture: Texture
) {
  private val xPix = spriteSize.toFloat / texture.width.toFloat
  private val yPix = spriteSize.toFloat / texture.height.toFloat
  private val spriteCache = mutable.Map.empty[String, SpriteImage]
  def getSprite(name: String): SpriteImage = {
    spriteCache.getOrElseUpdate(name, calculateSprite(name))
  }

  def calculateSprite(name: String): SpriteImage = {
    val (r, c) = spriteMap.getOrElse(
      name,
      throw new RuntimeException(s"sprite name $name not found for spriteSheetId $spriteSheetId")
    )
    val x1 = (r - 1) * xPix
    val x2 = r * xPix
    val y1 = (c - 1) * yPix
    val y2 = c * yPix
    SpriteImage(texture, SpriteOffset(x1, x2, y1, y2))
  }
}

class SpriteLoader extends TextureLoader with Logger {
  private val spriteSheets = mutable.Map.empty[String, SpriteSheet]
  private val textureCache = mutable.Map.empty[String, Texture]

  def loadSpriteSheet(
      spriteSheetId: String,
      spriteSheetPath: String,
      spriteSize: Int,
      spriteMap: Map[String, (Int, Int)] // "monster" -> (row 2, column 4)
  ): SpriteSheet = {
    def storeSpriteSheet() = {
      val texture = textureCache.getOrElseUpdate(spriteSheetPath, loadTexture(spriteSheetPath))
      validateMap(texture)

      new SpriteSheet(spriteSheetId, spriteSize, spriteMap, texture)
    }

    def validateMap(texture: Texture): Unit = {
      val Texture(_, width, height) = texture
      spriteMap.foreach {
        case (str, (r, c)) =>
          if ((r * spriteSize) > width)
            logWarn(s"spriteSheet [$spriteSheetId] sprite [$str] row [$r] is out of bounds")
          if ((c * spriteSize) > height)
            logWarn(s"spriteSheet [$spriteSheetId] sprite [$str] column [$c] is out of bounds")
      }
    }

    spriteSheets.getOrElseUpdate(spriteSheetId, storeSpriteSheet())
  }
}
