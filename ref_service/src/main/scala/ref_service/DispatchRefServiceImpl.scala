package ref_service

import java.sql.Timestamp
import java.util.UUID

import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch.model.{DispatchInfo, Request}
import io.grpc.stub.StreamObserver
import rx.lang.scala.subjects.PublishSubject
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class DispatchRefServiceImpl()(implicit ec: ExecutionContext) extends DispatchRefService  {

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
          expiryAt = Timestamp.valueOf(request.dispatchInfo.expiryAt),
          retriesCount = request.dispatchInfo.retriesCount
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

}