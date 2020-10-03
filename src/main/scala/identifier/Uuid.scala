package identifier

import java.util.UUID

trait Uuid {
  lazy val uuid: String = UUID.randomUUID().toString
}
