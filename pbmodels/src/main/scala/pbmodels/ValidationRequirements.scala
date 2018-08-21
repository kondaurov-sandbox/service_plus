package pbmodels

import dispatch.model.DispatchInfo

object ValidationRequirements {

  def dispatchInfo(cc: DispatchInfo): List[(Boolean, String)] = {
    List(
      cc.recipient.startsWith("7") -> "Recipient must begin with 7"
    )
  }

}
