package io.hoots.fingerprint

import java.time.Instant

import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler}
import akka.stream.{Attributes, Inlet, SinkShape}
import io.hoots.fingerprint.domain.{FingerPrint, HashedChunk}

/**
  * Created by rwadowski on 29.04.17.
  */
class FingerPrintSink(start: Instant) extends GraphStage[SinkShape[HashedChunk]] {

  val in: Inlet[HashedChunk] = Inlet("hashChunk")

  override val shape: SinkShape[HashedChunk] = SinkShape(in)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {

      private var print: FingerPrint = FingerPrint(null, Map.empty, null)

      override def preStart(): Unit = pull(in)

      setHandler(in, new InHandler {
        override def onPush(): Unit = {
          val chunk = grab(in)
          print = print + chunk
          pull(in)
        }

        override def onUpstreamFinish(): Unit = {
          val stop = Instant.now
          println(s"Duration ${java.time.Duration.between(start, stop).toMillis} ms")
        }
      })
    }
  }
}
