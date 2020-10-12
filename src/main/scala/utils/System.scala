package utils

import logging.Logger

object System extends Logger {
  private lazy val osName = {
    val name = java.lang.System.getProperty("os.name").toLowerCase()
    log(s"osName: $name")
    name
  }

  lazy val isMacOs: Boolean = {
    val isMac = osName.startsWith("mac os x")
    log(s"mac?: $isMac")
    isMac
  }
}
