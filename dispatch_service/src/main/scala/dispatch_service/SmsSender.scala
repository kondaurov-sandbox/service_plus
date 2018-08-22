package dispatch_service

import java.time.LocalDateTime

import dispatch.model.DispatchInfo
import org.json4s.JsonAST.JObject
import org.json4s.jackson.JsonMethods
import scalaj.http.{Http, HttpOptions}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.Breaks._

object SmsSender {

  case class Config(
      sendMsgUrl: String,
      readTimeout: Int
  )

}

class SmsSender(
    config: SmsSender.Config
) {

  import org.json4s.JsonDSL._

  private val http = Http(config.sendMsgUrl).option(HttpOptions.readTimeout(config.readTimeout))

  def req2Json(req: DispatchInfo): JObject = {
    ("recipient" -> req.recipient) ~
    ("message" -> req.content) ~
    ("channel" -> req.channel.name)
  }

  def sendMsg(req: DispatchInfo, alwaysSuccess: Option[Boolean] = None)(implicit ec: ExecutionContext): Future[Either[String, String]] = Future {

    var counter = 0

    val body = JsonMethods.compact(req2Json(req) ~ ("alwaysSuccess" -> alwaysSuccess))

    var result: Either[String, String] = Left("Msg isn't sent")

    breakable {

      if (LocalDateTime.now().isAfter(req.expiryAt.date)) {
        result = Left("Msg is expired")
        break()
      }

      while (counter <= req.retriesCount) {

        val resp = http.postData(body).asString

        if (resp.code == 200) {
          result = Right(resp.body)
          break()
        }

        if (resp.code == 404) {
          result = Left("Wrong url")
          break()
        }

        counter += 1

      }

    }

    result

  }

}
