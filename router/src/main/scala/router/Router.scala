package router

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch.model.Request.CreateDispatch

import scala.concurrent.{ExecutionContext, Future}
import common.akka_http.Directives._
import dispatch.model.{DispatchInfo, Request}

import scalapb.json4s.JsonFormat

class Router(
  refService: DispatchRefService
)(implicit ec: ExecutionContext) {

  val main: Route = {
    pathEndOrSingleSlash {
      complete("Welcome to dispatch router")
    } ~
    pathPrefix("api") {
      pathPrefix("dispatch") {
        (path("create") & post & withEntity[Models.CreateDispatch]) { model =>

          val newId = for {
           req <- Future.fromTry(model.toProto)
           newId <- refService.create(CreateDispatch(req))
          } yield newId.id.toString

          insideFuture(newId) { id =>
            complete(id)
          }

        } ~
        path("hi") { complete("Hi!") } ~
        (path("getStatus") & post & withEntity[Models.GetDispatchStatus]) { req =>

          val f = refService.getStatus(Request.GetStatus(id = DispatchInfo.Id(req.id)))

          insideFuture(f) { status =>
            complete(JsonFormat.toJsonString(status))
          }

        }
      }
    }
  }

}