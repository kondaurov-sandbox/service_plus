package dispatch_service.actors

import akka.actor.Actor
import dispatch.model.DispatchInfo

object Worker {

  case class DispatchMessage(body: DispatchInfo.WithId)

}

class Worker extends Actor {

  import Worker._

  override def receive: Receive = {

    case DispatchMessage(body) =>

      sender ! "'ok'"

    case _ => sender ! "'unknow message'"

  }

}
