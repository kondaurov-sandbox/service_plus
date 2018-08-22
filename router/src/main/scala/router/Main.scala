package router

import com.typesafe.config.ConfigFactory
import common.akka_http.HttpServerContext
import dispatch.model.DispatchRefServiceGrpc
import io.grpc.ManagedChannelBuilder

object Main extends App {

  val config = ConfigFactory.load()

  val config_ts = ConfigReader.getRouterConfig(config)

  val refServiceChannel = ManagedChannelBuilder.forAddress(
    config_ts.refService.host,
    config_ts.refService.port
  ).usePlaintext(true).build()

  val refService = DispatchRefServiceGrpc.stub(refServiceChannel)

  val server = new HttpServerContext()

  val router = new Router(
    refService = refService,
  )(server.executionContext)

  server.startServer(router.main)

}
