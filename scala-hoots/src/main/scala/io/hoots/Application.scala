package io.hoots

import java.io.File
import java.time.{Duration, Instant}
import java.util.UUID
import javax.sound.sampled.AudioSystem

import io.hoots.`match`.{BasicAnalyzer, Matcher}
import io.hoots.fingerprint.FingerPrinter
import io.hoots.input.WaveAudioFileReader
import io.hoots.input.domain.Item

import scala.collection.JavaConverters._

/**
  * Created by rwadowski on 04.04.17.
  */
object Application {

  def main(args: Array[String]): Unit = {

    val file1 = new File("/home/rwadowski/Downloads/poranek_kojota.wav")
    val file2 = new File("/home/rwadowski/Downloads/house_of_rising_sun.wav")
    val file3 = new File("/home/rwadowski/Downloads/unforgiven.wav")
    val file4 = new File("/home/rwadowski/Downloads/akatsuki.wav")
    val file5 = new File("/home/rwadowski/Downloads/house_of_rising_sun.wav")
    val file6 = new File("/home/rwadowski/Downloads/killer2.wav")
    val file7 = new File("/home/rwadowski/Downloads/chlopaki.wav")
    val sample = new File("/home/rwadowski/Downloads/poranek_kojota_sample_1.wav")

    AudioSystem.getAudioFileTypes.foreach(println)

    val l = AudioSystem.getAudioFileTypes()
    println(s"Supported types -> ${l.length}")

    val files = Seq(file1, file2, file3, file4, file5, file6, file7)
    val printer = new FingerPrinter()
    val matcher = new Matcher()
    val reader = new WaveAudioFileReader()
    val map = for(f <- files) yield {
      val signature = reader.streamFromFile(f)
      val result = printer.process(signature)
      matcher.update(result)
      signature.getItem -> f
    }
    val sampleResult = printer.process(reader.streamFromFile(sample))

    val analyzer = new BasicAnalyzer
    val start = Instant.now()
    val matches = matcher.process(sampleResult)
    val result = analyzer.process(matches)
    val end = Instant.now()

    println(s"Duration ${Duration.between(start, end).toMillis} ms")
    val res = result.getScores.asScala
    println(s"Scores for sample -> ${sample.getName}:")
    println(s"Found for sample -> ${res.maxBy(_._2.getRank)}:")
    for((item, f) <- map) {
      println(s"File -> ${f.getName} -> ${res(item)}")
    }
    println(s"Timestamps ${result.getMatchTimestamps.asScala}")
  }
}
