package router

import java.util.UUID

import dispatch.model.DispatchInfo
import dispatch.model.Response
import org.scalatest.{FlatSpec, Matchers, TryValues}
import pbmodels.{Processor, ValidationRequirements}

class ProcessorSpec extends FlatSpec with TryValues with Matchers {

  "json2cc" should "Response" in {
    Processor.json2cc[Response]("""{"result": "OK"}""")().success.value.result shouldBe Response.Result.OK
  }

  "str uuid" should "be parsable" in {

    val actual = Processor.json2cc[DispatchInfo.Id](
      """
        |"id": "2bfac73f-78b8-43a0-aaa8-8ffe6e219dcb"
      """.stripMargin
    )()

    actual.success.value.id shouldBe UUID.fromString("2bfac73f-78b8-43a0-aaa8-8ffe6e219dcb")

  }

  "str2cc" should "DispatchInfo" in {

    val actual = Processor.json2cc[DispatchInfo](
      """
        |{
        |"recipient": "71234",
        |"channel": "VIBER",
        |"content": "hello",
        |"expiryAt": "2018-08-21 22:00:00",
        |"retriesCount": 5
        |}
      """.stripMargin)(ValidationRequirements.dispatchInfo)

    actual.success.value.content shouldBe "hello"

  }

}
