package router

import com.typesafe.config.Config

import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.ceedubs.ficus.Ficus._

object ConfigReader {

  def getRouterConfig(config: Config): RouterConfig = {
    config.getConfig("router").as[RouterConfig]
  }

}
