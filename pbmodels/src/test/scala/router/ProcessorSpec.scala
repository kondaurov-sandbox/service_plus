package router

import dispatch.model.DispatchInfo
import dispatch.model.Response.NumMsg
import org.scalatest.{FlatSpec, Matchers, TryValues}
import pbmodels.{Processor, ValidationRequirements}

class ProcessorSpec extends FlatSpec with TryValues with Matchers {

  "json2cc" should "NumMsg" in {
    Processor.json2cc[NumMsg]("""{"num": 3}""")().success.value.num shouldBe 3
  }

  "str2cc" should "DispatchInfo" in {

    val actual = Processor.json2cc[DispatchInfo](
      """
        |{
        |"recepient": "71234",
        |"channel": "VIBER",
        |"content": "hello",
        |"expiryAt": "123s",
        |"retriesCount": 5
        |}
      """.stripMargin)(ValidationRequirements.dispatchInfo)

    actual.success.value.content shouldBe "hello"

  }

}
