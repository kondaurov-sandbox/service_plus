package dispatch_service

import java.time.LocalDateTime

import dispatch.model.DispatchInfo
import org.scalatest.{AsyncFlatSpec, EitherValues, Matchers}

class SmsSenderTest extends AsyncFlatSpec with Matchers with EitherValues {

  private val smsSender = new SmsSender(SmsSender.Config("http://localhost:9011/api/sendMsg"))

  val req = DispatchInfo(
    recipient = "78345",
    channel = DispatchInfo.channelType.fromName("VIBER").get,
    content = "hello",
    expiryAt = DispatchInfo.Date(date = LocalDateTime.now().plusDays(1)),
    retriesCount = 3
  )

  "send msg" should "work" in {

    val f = smsSender.sendMsg(req, alwaysSuccess = Some(true))

    f.map(resp => {
      resp shouldBe 'right
    })

  }

}
