package sms_gateway

object Models {

  case class SendMessage(
    recipient: String,
    channel: String,
    message: String,
    alwaysSuccess: Option[Boolean]
  )

}
