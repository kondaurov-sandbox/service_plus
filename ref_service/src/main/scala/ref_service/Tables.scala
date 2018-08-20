package ref_service

import java.util.UUID

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

object Tables {

  val db = Database.forConfig("db")

  object Dispatch {

    case class Record(
      id: UUID,
      recepient: String,
      channel: String,
      content: String,
      expiryAt: String,
      retriesCount: Int
    )

    class DispatchTable(tag: Tag) extends Table[Record](tag, "dispatch") {

      def id = column[UUID]("id", O.PrimaryKey)
      def recepient = column[String]("recepient")
      def channel = column[String]("channel")
      def content = column[String]("content")
      def expiryAt = column[String]("expiry_at")
      def retriesCount = column[Int]("retries_count")

      override def * = (id, recepient, channel, content, expiryAt, retriesCount) <> (Record.tupled, Record.unapply)
    }

    lazy val table = TableQuery[DispatchTable]


  }


}
