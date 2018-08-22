package sms_gateway

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object SmsSender {

  case class Config(
    maxRandom: Int,
    failWhenLess: Int,
    waitSeconds: Int
  )

}

class SmsSender(
    config: SmsSender.Config
)(implicit ec: ExecutionContext) {

  val random = Random

  def send(msg: Models.SendMessage): Future[String] = Future {

    for {
      _ <- {

        if (config.waitSeconds > 0) {
          println("wait")
          Thread.sleep(config.waitSeconds * 1000)
        }

        val rand = random.nextInt(config.maxRandom)

        if (!msg.alwaysSuccess.contains(true) && rand < config.failWhenLess) {
          Future.failed(new Exception("failed"))
        } else {
          Future.successful("ok")
        }
      }
      resp <- Future.successful("success")
    } yield resp

  }.flatten

}
