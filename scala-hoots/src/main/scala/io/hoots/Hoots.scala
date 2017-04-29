package io.hoots

import java.io.File
import java.time.Instant

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.scaladsl.{Balance, Flow, GraphDSL, Merge, Source}
import akka.stream.stage.GraphStage
import akka.stream.{ActorMaterializer, SourceShape}
import io.hoots.fft.{DefaultFFTFactory, JTransformFFT}
import io.hoots.fingerprint.domain._
import io.hoots.fingerprint.{ChunkPrinter, DefaultHashBuilder}
import io.hoots.input.FileReaderSource
import io.hoots.signature.{WaveAudioFileReader, WaveFileReaderDefaults}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * Created by rwadowski on 26.04.17.
  */
class Hoots(systemName: String = "HootsActorSystem") {

  private implicit val system = ActorSystem(systemName)
  private implicit val ec = system.dispatchers.lookup("print-dispatcher")

  private implicit val actorMat = ActorMaterializer()

  val fuzzFactor = FuzzFactor(3)
  val printerConfig = PrinterConfig(upperLimit = UpperLimit(300),
    lowerLimit = LowerLimit(40),
    chunkSize = ChunkSize(4096),
    fuzzFactor = fuzzFactor)
  val hashBuilder = new DefaultHashBuilder(fuzzFactor)
  val fftFactory = new DefaultFFTFactory
  val reader = new WaveAudioFileReader

  def testGraph(): Unit = {
    val fft = new JTransformFFT
    val chunkPrinter = new ChunkPrinter(hashBuilder, printerConfig)
    val file = new File("/home/rwadowski/Downloads/house_of_rising_sun.wav")
    val start = Instant.now
    val sourceGraph: GraphStage[SourceShape[ChunkOfBytes]] = new FileReaderSource(file, WaveFileReaderDefaults.outputFormat, printerConfig.chunkSize)
    val source = Source.fromGraph(sourceGraph)
    val g = Source.fromGraph(GraphDSL.create() { implicit builder: GraphDSL.Builder[NotUsed] =>
      import GraphDSL.Implicits._
      val in = source
      val cnt = 8
      val balance = builder.add(Balance[ChunkOfBytes](cnt))
      val merge = builder.add(Merge[HashedChunk](cnt))
      val fftFlow: Flow[ChunkOfBytes, ChunkOfComplex, NotUsed] = Flow[ChunkOfBytes].map(fft.transform)
      val hashFlow: Flow[ChunkOfComplex, HashedChunk, NotUsed] = Flow[ChunkOfComplex].map{chunkPrinter.print}
      in ~> balance.in
      (0 until cnt).foreach(i => balance.out(i) ~> fftFlow.async ~> hashFlow.async ~> merge)
      SourceShape(merge.out)
    })
    val ff = g.runFold(FingerPrint(null, Map.empty, null))((fp, chunk) => fp + chunk)
    Await.result(ff.map{_ =>
      val stop = Instant.now
      println(s"Duration ${java.time.Duration.between(start, stop).toMillis} ms")
      system.terminate()
    }, 1000 second)
  }

  def shutdown(): Unit = {
    system.terminate()
  }
}
