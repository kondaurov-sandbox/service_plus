package sms_gateway

import com.typesafe.config.ConfigFactory
import common.akka_http.HttpServerContext

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  val config = ConfigFactory.load()

  val config_ts = ConfigReader.getRouterConfig(config.getConfig("smsGateway"))

  val server = new HttpServerContext()

  val smsSender = new SmsSender(config_ts.smsSender)

  val router = new Router(smsSender)(server.executionContext)

  server.startServer(router.main)

}
