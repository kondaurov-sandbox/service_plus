package sms_gateway

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

import common.akka_http.Directives._

class Router(
    smsSender: SmsSender
)(implicit ec: ExecutionContext) {

  val main: Route = {
    pathEndOrSingleSlash {
      complete("Welcome to sms gateway")
    } ~
    pathPrefix("api") {
      (post & pathPrefix("sendMsg") & withEntity[Models.SendMessage]) { body =>
        val resp = for {
          resp <- smsSender.send(body)
        } yield resp

        onComplete(resp) {
          case Success(res) => complete(res)
          case Failure(err) => complete(HttpResponse(status = StatusCodes.BadRequest, entity = s"error: ${err.getMessage}"))
        }
      }
    }
  }

}