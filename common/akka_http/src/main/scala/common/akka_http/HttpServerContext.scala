package common.akka_http

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.Future
import scala.util.{Failure, Success}

class HttpServerContext() {

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  var bindingFuture: Future[Http.ServerBinding] = _

  def startServer(routes: Route) = {
    bindingFuture = Http().bindAndHandle(routes, "0.0.0.0", 8080)
    bindingFuture.onComplete {
      case Success(res) => println(s"Server online at http://localhost:8080/\n...")
      case Failure(err) => println(s"Can't start server: ${err.getMessage}")
    }
  }



}
