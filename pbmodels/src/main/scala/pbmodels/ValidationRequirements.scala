package pbmodels

import dispatch.model.DispatchInfo

object ValidationRequirements {

  def dispatchInfo(cc: DispatchInfo): List[(Boolean, String)] = {
    List(
      cc.recipient.startsWith("7") -> "Recipient must begin with 7",
      (cc.content.length <= 1000) -> "Content must be less then 1000 symbols"
    )
  }

}
