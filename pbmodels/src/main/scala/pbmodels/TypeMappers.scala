package pbmodels

import java.time.LocalDateTime
import java.util.UUID

import scalapb.TypeMapper

object TypeMappers {

  implicit val localDateTime: TypeMapper[String, LocalDateTime] = {
    TypeMapper[String, LocalDateTime](s => {
      if (s.isEmpty) LocalDateTime.now() else Parser.parseDatetime(s).get
    })(dt => dt.format(Parser.formatter))
  }

  implicit val uuid: TypeMapper[String, UUID] = {
    TypeMapper[String, UUID](s => {
      if (s.isEmpty) UUID.fromString("00000000-0000-0000-0000-000000000000") else UUID.fromString(s)
    })(uuid => uuid.toString)
  }

}
