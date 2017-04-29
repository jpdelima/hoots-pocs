package io.hoots.input

import java.io.File
import javax.sound.sampled.{AudioFormat, AudioInputStream, AudioSystem}

import akka.stream.stage.{GraphStage, GraphStageLogic, OutHandler}
import akka.stream.{Attributes, Outlet, SourceShape}
import io.hoots.fingerprint.domain.{ChunkNumber, ChunkOfBytes, ChunkSize}
import io.hoots.input.domain.Item
import io.hoots.signature.WaveFileReaderDefaults
import org.tritonus.sampled.convert.PCM2PCMConversionProvider

/**
  * Created by rwadowski on 28.04.17.
  */
class FileReaderSource(file: File,
                       format: AudioFormat,
                       chunkSize: ChunkSize) extends GraphStage[SourceShape[ChunkOfBytes]] {

  val out: Outlet[ChunkOfBytes] = Outlet("chunk")

  override val shape: SourceShape[ChunkOfBytes] = SourceShape(out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = {
    new GraphStageLogic(shape) {

      private val conversionProvider = new PCM2PCMConversionProvider
      private var currentChunkNumber = 0
      private var stream: AudioInputStream = _
      private var din: AudioInputStream = _
      private var in: AudioInputStream = _
      val item = Item(file.getName)

      override def preStart(): Unit = {
        super.preStart()
        in = AudioSystem.getAudioInputStream(file)
        val baseFormat = in.getFormat
        val decodedFormat = convertToDecodedFormat(baseFormat)
        din = AudioSystem.getAudioInputStream(decodedFormat, in)
        stream = conversionProvider.getAudioInputStream(format, din)
      }

      override def postStop(): Unit = {
        super.postStop()
        stream.close()
        din.close()
        in.close()
      }

      private def convertToDecodedFormat(format: AudioFormat): AudioFormat =
        new AudioFormat(WaveFileReaderDefaults.decodedEncoding, format.getSampleRate, WaveFileReaderDefaults.decodedSampleSizeInBits, format.getChannels, format.getChannels * 2, format.getSampleRate, WaveFileReaderDefaults.decodedBigEndian)

      setHandler(out, new OutHandler {
        override def onPull(): Unit = {
          val buffer = Array.ofDim[Byte](chunkSize.value)
          val bytesRead = stream.read(buffer, 0, chunkSize.value)
          if(bytesRead > 0) {
            push(out, ChunkOfBytes(ChunkNumber(currentChunkNumber), chunkSize, item, buffer.toList))
            currentChunkNumber = currentChunkNumber + 1
          } else {
            complete(out)
          }
        }
      })
    }
  }
}
