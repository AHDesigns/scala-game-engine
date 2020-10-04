package logging

trait Logger {
  private def consoleLog(prefix: String, msg: String): Unit =
    System.out.print(prefix + Console.RESET + " | " + msg + "\n")
  protected def log(msg: String): Unit =
    consoleLog(Console.CYAN + "INFO", msg)
  protected def logWarn(msg: String): Unit =
    consoleLog(Console.YELLOW + "WARN", msg)
  protected def logErr(msg: String): Unit =
    consoleLog(Console.RED + "ERR!", msg)
}
