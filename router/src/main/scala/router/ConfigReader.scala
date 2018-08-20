package router

import com.typesafe.config.Config
import net.ceedubs.ficus.readers.ValueReader

import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.ceedubs.ficus.Ficus._

object ConfigReader {

  def getTypedConfig[C](path: String, config: Config)(implicit reader: ValueReader[C]): C = {

    config.getConfig(path).as[C]

  }

  def getRouterConfig(config: Config): RouterConfig = {
    getTypedConfig[RouterConfig]("router", config)
  }

}
