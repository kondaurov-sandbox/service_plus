package dispatch_service

object DispatchConfig {

  case class ServicePath(
    host: String,
    port: Int
  )

}

case class DispatchConfig(
  refService: DispatchConfig.ServicePath
)
