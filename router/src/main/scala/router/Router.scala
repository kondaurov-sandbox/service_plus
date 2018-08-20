package router

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import dispatch.model.DispatchInfo
import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch.model.Request.CreateDispatch
import pbmodels.{Processor, ValidationRequirements}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class Router(
  refService: DispatchRefService
)(implicit ec: ExecutionContext) {

  val main: Route = {
    pathEndOrSingleSlash {
      complete("Welcome to sms dispatcher")
    } ~
    pathPrefix("api") {
      pathPrefix("dispatch") {
        (post & path("create") & entity(as[String])) { body =>

          val newId = for {
           req <- Future.fromTry(Processor.json2cc[DispatchInfo](body)((d: DispatchInfo) => ValidationRequirements.dispatchInfo(d)))
           newId <- refService.create(CreateDispatch(req))
          } yield newId

          onComplete(newId) {
            case Success(id) => complete(id.toString)
            case Failure(err) => complete(HttpResponse(status = StatusCodes.BadRequest, entity = s"error: ${err.getMessage}"))
          }

        }
      }
    }
  }

}