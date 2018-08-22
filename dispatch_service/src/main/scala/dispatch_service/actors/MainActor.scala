package dispatch_service.actors

import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import dispatch.model.DispatchRefServiceGrpc.DispatchRefService
import dispatch_service.SmsSender

object MainActor {

  def props(smsSender: SmsSender, refService: DispatchRefService): Props = Props(new MainActor(smsSender, refService))

}

class MainActor(
  smsSender: SmsSender,
  refService: DispatchRefService
) extends Actor {

  var router: Router = {

    val routees = Vector.fill(3) {
      val r = context.actorOf(Worker.props(smsSender, refService))
      context watch r
      ActorRefRoutee(r)
    }

    Router(RoundRobinRoutingLogic(), routees)

  }

  override def receive: Receive = {

    case msg: Worker.DispatchMessage =>

      println(s"Send task to worker: ${msg.body.id}")
      router.route(msg, sender())

    case Terminated(a) =>

      router = router.removeRoutee(a)
      val r = context.actorOf(Props[Worker])
      context watch r
      router = router.addRoutee(r)

    case _ => sender ! "'Unknown message'"

  }

}
