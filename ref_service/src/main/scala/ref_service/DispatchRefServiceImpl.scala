package ref_service

import java.util.UUID

import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch.model.{DispatchInfo, Request, Response}
import io.grpc.stub.StreamObserver
import rx.lang.scala.subjects.PublishSubject

import slick.jdbc.PostgresProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class DispatchRefServiceImpl()(implicit ec: ExecutionContext) extends DispatchRefService  {

  private val newMessagePublisher = PublishSubject[DispatchInfo.Id]()

  def create(request: Request.CreateDispatch): Future[DispatchInfo.Id] = {

    val newId = UUID.randomUUID()

    val op = DBIO.seq(
      Tables.Dispatch.table += {
        Tables.Dispatch.Record(
          id = newId,
          channel = request.dispatchInfo.channel.name,
          recepient = request.dispatchInfo.recepient,
          content = request.dispatchInfo.content,
          expiryAt = request.dispatchInfo.expiryAt,
          retriesCount = request.dispatchInfo.retriesCount
        )
      }
    )

    Tables.db.run(op).map(_ => {
      val id = DispatchInfo.Id(newId.toString)
      newMessagePublisher.onNext(id)
      id
    })

  }

  def getNewMessage(request: Request.GetNewMessage, responseObserver: StreamObserver[DispatchInfo.Id]): Unit = {
    newMessagePublisher.subscribe(v => responseObserver.onNext(v))
  }

}













