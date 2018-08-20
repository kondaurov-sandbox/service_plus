package pbmodels

import dispatch.model.DispatchInfo

object ValidationRequirements {

  def dispatchInfo(cc: DispatchInfo): List[(Boolean, String)] = {
    List(
      cc.recepient.startsWith("7") -> "Recepient must begin with 7",
      Parser.parseDatetime(cc.expiryAt).isSuccess -> "ExpiryAt not a date"
    )
  }

}
