package dispatch_service.actors

import java.time.LocalDateTime

import akka.actor.{Actor, Props}
import dispatch.model.{DispatchInfo, DispatchResult, Request}
import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch_service.SmsSender

import scala.util.{Failure, Success}

object Worker {

  def props(smsSender: SmsSender, refService: DispatchRefService): Props = {
    Props(new Worker(smsSender, refService))
  }

  case class DispatchMessage(body: DispatchInfo.WithId)

}

class Worker(
  smsSender: SmsSender,
  refService: DispatchRefService
) extends Actor {

  import Worker._

  import context.dispatcher

  override def receive: Receive = {

    case DispatchMessage(body) =>

      val status = {
        Request.SetStatus(
          id = body.id,
          status = DispatchResult(
            id = body.id,
            status = DispatchResult.Status.INPROGRESS,
            errorMsg = None,
            deliveredAt = None
          )
        )
      }

      val f = for {
        _ <- refService.setStatus(status)
        resp <- smsSender.sendMsg(body.info)(dispatcher).recover { case err => Left(err.getMessage)}
      } yield resp

      f.onComplete {
        case Success(Right(res)) =>
          refService.setStatus(status.copy(status = status.status.copy(
            deliveredAt = Some(DispatchInfo.Date(LocalDateTime.now())),
            status = DispatchResult.Status.SUCCESS
          )))
          println(s"msg has been sent ${body.id.id.toString}: $res :)")
        case Success(Left(err)) =>
          refService.setStatus(status.copy(status = status.status.copy(
            status = DispatchResult.Status.FAILED,
            errorMsg = Some(err)
          )))
          println(s"msg's send error ${body.id.id.toString} $err :)")
        case Failure(err) => println(s"Can't send msg ${body.id.id.toString}. ${err.getMessage} :(")
      }

      sender ! "'ok'"

    case _ => sender ! "'unknow message'"

  }

}
