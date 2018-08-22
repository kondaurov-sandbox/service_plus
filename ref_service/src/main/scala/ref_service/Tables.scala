package ref_service

import java.sql.Timestamp
import java.util.UUID

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

object Tables {

  val db = Database.forConfig("db")

  object Dispatch {

    case class Record(
      id: UUID,
      recipient: String,
      channel: String,
      content: String,
      expiryAt: Timestamp,
      retriesCount: Int
    )

    class DispatchTable(tag: Tag) extends Table[Record](tag, "dispatch") {

      def id = column[UUID]("id", O.PrimaryKey)
      def recipient = column[String]("recipient")
      def channel = column[String]("channel")
      def content = column[String]("content")
      def expiryAt = column[Timestamp]("expiry_at")
      def retriesCount = column[Int]("retries_count")

      override def * = (id, recipient, channel, content, expiryAt, retriesCount) <> (Record.tupled, Record.unapply)
    }

    val table = TableQuery[DispatchTable]

  }

  object DispatchResult {

    case class Record(
        id: UUID,
        status: String,
        deliveredAt: Option[Timestamp],
        errorMsg: Option[String]
    )

    class DispatchResultTable(tag: Tag) extends Table[Record](tag, "dispatch_result") {

      def id = column[UUID]("id", O.PrimaryKey)
      def status = column[String]("status")
      def deliveredAt = column[Timestamp]("delivered_at")
      def errorMsg = column[String]("error_msg")

      override def * = (id, status, deliveredAt.?, errorMsg.?) <> (Record.tupled, Record.unapply)

    }

    val table = TableQuery[DispatchResultTable]

  }


}
