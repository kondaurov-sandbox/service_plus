package ref_service

import com.typesafe.config.ConfigFactory
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, Matchers}
import slick.jdbc.PostgresProfile.api._

class TablesTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll {

  "from table dispatch" should "retrieve some records" in {

    val req = Tables.Dispatch.table.take(1).result

    val resp = Tables.db.run(req)

    resp.map(res => {
      assert(res.length >= 0)
    })

  }

  override protected def beforeAll(): Unit = {
    val config = ConfigFactory.load()

    val config_ts = ConfigReader.getRefServiceConfig(config)

    val migrator = new DbMigratorContext(config_ts)

    migrator.flyway.migrate()

  }

}
