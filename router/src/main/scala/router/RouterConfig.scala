package router

object RouterConfig {

  case class ServicePath(
    host: String,
    port: Int
  )

}

case class RouterConfig(
  refService: RouterConfig.ServicePath
)
