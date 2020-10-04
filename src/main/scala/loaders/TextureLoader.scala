package loaders

import java.nio.ByteBuffer

import org.lwjgl.opengl.GL11._
import org.lwjgl.stb.STBImage.{stbi_failure_reason, stbi_image_free, stbi_load}
import org.lwjgl.system.MemoryStack

trait TextureLoader {
  // from https://ahbejarano.gitbook.io/lwjglgamedev/chapter7
  def loadTexture(filePath: String): Int = {

    val absoluteFilePath = "res/" + filePath
    var width = 0
    var height = 0
    var buf: ByteBuffer = null
    // Load Texture file
    val stack = MemoryStack.stackPush
    try {
      val w = stack.mallocInt(1)
      val h = stack.mallocInt(1)
      val channels = stack.mallocInt(1)
      buf = stbi_load(absoluteFilePath, w, h, channels, 4)
      if (buf == null)
        throw new Exception(
          "Image file [" + absoluteFilePath + "] not loaded: " + stbi_failure_reason
        )
      /* Get width and height of image */
      width = w.get
      height = h.get
    } catch {
      case e: Throwable => e.printStackTrace()
    } finally if (stack != null) stack.close()
    // Create a new OpenGL texture

    val textureId = glGenTextures
    // Bind the texture
    glBindTexture(GL_TEXTURE_2D, textureId)
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

    // filters?
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
//    glGenerateMipmap(GL_TEXTURE_2D)

    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
    stbi_image_free(buf)
    textureId
  }

}
