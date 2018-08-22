package pbmodels

import org.json4s.JValue
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}
import scalapb.json4s.JsonFormat

import scala.util.{Failure, Success, Try}

object Processor {

  def json2cc[C <: GeneratedMessage with Message[C] : GeneratedMessageCompanion](json: JValue): Try[C] = {
    for {
      cc <- Try(JsonFormat.fromJson[C](json)).recoverWith { case err => Failure(new Exception(s"Wrong input: ${err.getMessage}"))}
    } yield cc
  }

}
