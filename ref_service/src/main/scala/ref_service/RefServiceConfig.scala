package ref_service

object RefServiceConfig {

  case class Db(
    url: String,
    user: String,
    password: String
  )

}

case class RefServiceConfig(
    db: RefServiceConfig.Db
)
