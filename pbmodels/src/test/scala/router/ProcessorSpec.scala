package router

import java.util.UUID

import dispatch.model.DispatchInfo
import dispatch.model.Response
import org.json4s.JsonAST._
import org.scalatest.{FlatSpec, Matchers, TryValues}
import pbmodels.Processor

class ProcessorSpec extends FlatSpec with TryValues with Matchers {

  "json2cc" should "Response" in {
    Processor.json2cc[Response](JObject("result" -> JString("OK"))).success.value.result shouldBe Response.Result.OK
  }

  "str uuid" should "be parsable" in {

    val obj = JObject(
      "id" -> JString("2bfac73f-78b8-43a0-aaa8-8ffe6e219dcb")
    )

    val actual = Processor.json2cc[DispatchInfo.Id](obj)

    actual.success.value.id shouldBe UUID.fromString("2bfac73f-78b8-43a0-aaa8-8ffe6e219dcb")

  }

  "str2cc" should "DispatchInfo" in {

    val obj = JObject(
      "recipient" -> JString("71234"),
      "channel" -> JString("VIBER"),
      "content" -> JString("hello"),
      "expiryAt" -> JString("2018-08-21 22:00:00"),
      "retiresCount" -> JInt(5)
    )

    val actual = Processor.json2cc[DispatchInfo](obj)

    actual.success.value.content shouldBe "hello"

  }

}
