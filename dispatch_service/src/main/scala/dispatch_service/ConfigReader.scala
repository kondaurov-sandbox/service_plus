package dispatch_service

import net.ceedubs.ficus.readers.ValueReader

import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.ceedubs.ficus.Ficus._

import com.typesafe.config.Config

object ConfigReader {

  def getTypedConfig[C](path: String, config: Config)(implicit reader: ValueReader[C]): C = {

    config.getConfig(path).as[C]

  }

  def getDispatchConfig(config: Config): DispatchConfig = {
    getTypedConfig[DispatchConfig]("dispatch", config)
  }

}
