package ref_service

import com.typesafe.config.ConfigFactory
import dispatch.model.DispatchRefServiceGrpc
import io.grpc.ServerBuilder

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {

  Tables // init

  val config = ConfigFactory.load()

  val config_ts = ConfigReader.getRefServiceConfig(config)

  val migrator = new DbMigratorContext(config_ts)

  migrator.flyway.migrate()

  val srv = DispatchRefServiceGrpc.bindService(new DispatchRefServiceImpl(), global)

  val server = ServerBuilder.forPort(9010).addService(srv).build()

  server.start()

  System.out.println("Server started on port 9010...")

  server.awaitTermination()

  sys.addShutdownHook {
    System.err.println("*** shutting down gRPC server since JVM is shutting down")
    server.shutdown()
    System.err.println("*** server shut down")
  }


}
