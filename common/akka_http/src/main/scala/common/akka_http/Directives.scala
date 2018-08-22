package common.akka_http

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import org.json4s._
import org.json4s.JsonAST.JNull
import org.json4s.jackson.JsonMethods.parse

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Directives {

  implicit val formats = DefaultFormats

  def withJson: Directive1[JValue] = {
    extractRequest.flatMap { req =>
      if (req.entity.isKnownEmpty()) provide(JNull) else {
        entity(as[String]).flatMap(body => {
          val parsed = Try(parse(body))
          onComplete(Future.fromTry(parsed)).flatMap {
            case Success(res) => provide(res)
            case Failure(err) =>
              complete {
                HttpResponse(StatusCodes.BadRequest, entity = s"Incorrect json syntax: ${err.getMessage}")
              }
          }
        })
      }
    }
  }

  def withEntity[A](implicit formats: Formats, manifest: Manifest[A]): Directive1[A] = {

    withJson.flatMap { json =>

      onComplete(Future.fromTry(Try(json.extract[A]))).flatMap {
        case Success(res) => provide(res)
        case Failure(err) =>
          complete {
            HttpResponse(StatusCodes.BadRequest, entity = s"Wrong input: ${err.getMessage}")
          }
      }

    }

  }

}
