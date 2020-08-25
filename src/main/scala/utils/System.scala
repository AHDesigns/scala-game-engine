package utils

object System {
  private lazy val osName = {
    val name = java.lang.System.getProperty("os.name").toLowerCase()
    println(s"osName: $name")
    name
  }

  lazy val isMacOs: Boolean = {
    val isMac = osName.startsWith("mac os x")
    println(s"mac?: $isMac")
    isMac
  }
}
