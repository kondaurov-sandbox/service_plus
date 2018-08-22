package dispatch_service

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import dispatch.model.DispatchRefServiceGrpc
import dispatch.model.Request.GetNewMessage
import dispatch_service.actors.MainActor
import io.grpc.ManagedChannelBuilder

object Main extends App {

  val config = ConfigFactory.load()

  val config_ts = ConfigReader.getDispatchConfig(config)

  val actorSystem = ActorSystem("dispatch_service")

  val refServiceChannel = ManagedChannelBuilder.forAddress(
    config_ts.refService.host,
    config_ts.refService.port
  ).usePlaintext(true).build()

  val refService = DispatchRefServiceGrpc.stub(refServiceChannel)

  val smsSender = new SmsSender(config_ts.smsGateway)

  val mainActor = actorSystem.actorOf(MainActor.props(smsSender, refService), "main")

  refService.getNewMessage(GetNewMessage(), new Observer(mainActor = mainActor))

}
