package pbmodels

import scala.util.Try

object Parser {

  import java.time.LocalDateTime
  import java.time.format.DateTimeFormatter

  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

  def parseDatetime(str: String): Try[LocalDateTime] = Try {
    LocalDateTime.parse(str, formatter)
  }

}