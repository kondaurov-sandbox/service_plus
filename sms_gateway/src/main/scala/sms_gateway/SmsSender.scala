package sms_gateway

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object SmsSender {

  case class Config(
    maxRandom: Int,
    failWhenLess: Int
  )

}

class SmsSender(
    config: SmsSender.Config
)(implicit ec: ExecutionContext) {

  val random = Random

  def send(msg: Models.SendMessage): Future[String] = {

    for {
      _ <- {
        val rand = random.nextInt(config.maxRandom)

        if (rand < config.failWhenLess) {
          Future.failed(new Exception("failed"))
        } else {
          Future.successful("ok")
        }
      }
      resp <- Future.successful("success")
    } yield resp

  }

}
