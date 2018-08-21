package dispatch_service.actors

import akka.actor.{Actor, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}

object MainActor {

  def props: Props = Props[MainActor]()

}

class MainActor extends Actor {

  var router: Router = {

    val routees = Vector.fill(3) {
      val r = context.actorOf(Props[Worker])
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
