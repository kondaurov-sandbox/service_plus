package ref_service

import com.typesafe.config.Config
import net.ceedubs.ficus.Ficus._
import net.ceedubs.ficus.readers.ArbitraryTypeReader._
import net.ceedubs.ficus.readers.ValueReader

object ConfigReader {

  def getTypedConfig[C](config: Config)(implicit reader: ValueReader[C]): C = {
    config.as[C]
  }

  def getRefServiceConfig(config: Config): RefServiceConfig = {
    getTypedConfig[RefServiceConfig](config)
  }

}
