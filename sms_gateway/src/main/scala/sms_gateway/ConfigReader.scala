package sms_gateway

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._

object ConfigReader {

  def getRouterConfig(config: Config): SmsGatewayConfig = {
    config.as[SmsGatewayConfig]
  }

}
