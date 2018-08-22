package ref_service

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.UUID

import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch.model.{DispatchInfo, DispatchResult, Request, Response}
import io.grpc.stub.StreamObserver
import rx.lang.scala.subjects.PublishSubject
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

import Conversions._

class DispatchRefServiceImpl()(implicit ec: ExecutionContext) extends DispatchRefService  {

  implicit def datetime2timestamp(dt: LocalDateTime): Timestamp = Timestamp.valueOf(dt)

  private val newMessagePublisher = PublishSubject[DispatchInfo.WithId]()

  def create(request: Request.CreateDispatch): Future[DispatchInfo.Id] = {

    val newId = UUID.randomUUID()

    val op = DBIO.seq(
      Tables.Dispatch.table += {
        Tables.Dispatch.Record(
          id = newId,
          channel = request.dispatchInfo.channel.name,
          recipient = request.dispatchInfo.recipient,
          content = request.dispatchInfo.content,
          expiryAt = request.dispatchInfo.expiryAt.date,
          retriesCount = request.dispatchInfo.retriesCount
        )
      },
      Tables.DispatchResult.table += {
        Tables.DispatchResult.Record(
          id = newId,
          status = DispatchResult.Status.FRESH.name,
          deliveredAt = None,
          errorMsg = None
        )
      }
    )

    Tables.db.run(op).map(_ => {
      val id = DispatchInfo.Id(newId)
      newMessagePublisher.onNext(DispatchInfo.WithId(
        id = id,
        info = request.dispatchInfo
      ))
      id
    })

  }

  def getNewMessage(request: Request.GetNewMessage, responseObserver: StreamObserver[DispatchInfo.WithId]): Unit = {
    newMessagePublisher.subscribe(v => responseObserver.onNext(v))
  }

  def setStatus(request: Request.SetStatus): Future[Response.Empty] = {

    val record =
      Tables.DispatchResult.Record(
        id = request.id.id,
        status = request.status.status.name,
        deliveredAt = request.status.deliveredAt.map(_.date),
        errorMsg = request.status.errorMsg
      )

    val op = DBIO.seq(
      Tables.DispatchResult.table.filter(_.id === request.id.id).update(record)
    )

    Tables.db.run(op).map(_ => Response.Empty())

  }

  def getStatus(request: Request.GetStatus): Future[DispatchResult] = {

    val op = Tables.DispatchResult.table.filter(_.id === request.id.id).result.head

    Tables.db.run(op).flatMap { res =>
      Future.fromTry(res.toProto)
    }

  }

}