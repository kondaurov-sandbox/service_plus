package dispatch_service

import com.typesafe.config.ConfigFactory
import dispatch.model.{DispatchInfo, DispatchRefServiceGrpc, Response}
import dispatch.model.Request.GetNewMessage
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver

object Main extends App {

  val config = ConfigFactory.load()

  val config_ts = ConfigReader.getDispatchConfig(config)

  val refServiceChannel = ManagedChannelBuilder.forAddress(
    config_ts.refService.host,
    config_ts.refService.port
  ).usePlaintext(true).build()

  val refService = DispatchRefServiceGrpc.stub(refServiceChannel)

  val observer = new StreamObserver[DispatchInfo.Id] {
    override def onNext(value: DispatchInfo.Id): Unit = println(s"got new message: ${value.id}")

    override def onError(t: Throwable): Unit = println("stream error")

    override def onCompleted(): Unit = println("streaming done")
  }

  refService.getNewMessage(GetNewMessage(), observer)

  Thread.currentThread.join()

}
