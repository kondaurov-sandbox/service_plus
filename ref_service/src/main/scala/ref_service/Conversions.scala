package ref_service

import dispatch.model.{DispatchInfo, DispatchResult}

import scala.util.Try

object Conversions {

  implicit class DispatchResultRich(val inner: Tables.DispatchResult.Record) extends AnyVal {

    def toProto: Try[DispatchResult] = Try {

      val id = DispatchInfo.Id(inner.id)
      val status = DispatchResult.Status.fromName(inner.status)
      val deliveredAt = inner.deliveredAt.map(ts => DispatchInfo.Date(ts.toLocalDateTime))

      if (status.isEmpty) throw new Exception("Unknown status")

      DispatchResult(
        id = id,
        status = status.get,
        deliveredAt = deliveredAt,
        errorMsg = inner.errorMsg
      )

    }

  }

}
