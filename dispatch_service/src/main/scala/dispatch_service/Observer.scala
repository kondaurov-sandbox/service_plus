package dispatch_service

import akka.actor.ActorRef
import dispatch.model.DispatchInfo
import dispatch_service.actors.Worker
import io.grpc.stub.StreamObserver

class Observer(
    mainActor: ActorRef
) extends StreamObserver[DispatchInfo.WithId] {

  def onNext(value: DispatchInfo.WithId): Unit = {
    println(s"got new message: ${value.id}")
    mainActor ! Worker.DispatchMessage(body = value)
  }

  def onError(t: Throwable): Unit = println(s"stream error: ${t.getMessage}")

  def onCompleted(): Unit = println("streaming done")

}
