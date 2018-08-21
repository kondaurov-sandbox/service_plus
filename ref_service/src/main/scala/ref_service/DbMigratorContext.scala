package ref_service

import org.flywaydb.core.Flyway

class DbMigratorContext(
  config: RefServiceConfig
) {

  val flyway = new Flyway()

  flyway.setDataSource(config.db.url, config.db.user, config.db.password)

}
