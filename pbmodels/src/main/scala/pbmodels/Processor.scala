package pbmodels

import org.json4s.jackson.JsonMethods.parse
import scalapb.{GeneratedMessage, GeneratedMessageCompanion, Message}
import scalapb.json4s.JsonFormat

import scala.util.{Failure, Success, Try}

object Processor {

  def json2cc[C <: GeneratedMessage with Message[C] : GeneratedMessageCompanion](s: String)(requirements: C => List[(Boolean, String)] = (_: C) => List()): Try[C] = {
    for {
      json <-Try(parse(s)).recoverWith { case err => Failure(new Exception(s"Wrong json: ${err.getMessage}")) }
      cc <- Try(JsonFormat.fromJson[C](json)).recoverWith { case err => Failure(new Exception(s"Wrong input: ${err.getMessage}"))}
      _ <- checkRequirements(requirements(cc))
    } yield cc
  }

  def checkRequirements[C](requirements: List[(Boolean, String)]): Try[Unit] = {
    val errors = requirements.filter(_._1 == false)

    if (errors.isEmpty) { Success({}) } else { Failure(new Exception(s"Validation error: ${errors.toString()}")) }
  }



}
