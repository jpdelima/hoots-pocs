package io.hoots

import java.io.File
import java.time.{Duration, Instant}
import javax.sound.sampled.AudioSystem

import io.hoots.results.Analyzer
import io.hoots.results.domain.Matcher
import io.hoots.fingerprint.FingerPrinter
import io.hoots.signature.WaveAudioFileReader

/**
  * Created by rwadowski on 04.04.17.
  */
object Application {

  def main(args: Array[String]): Unit = {

    val file1 = new File("/home/rwadowski/Downloads/house_of_rising_sun.wav")
    val file2 = new File("/home/rwadowski/Downloads/unforgiven.wav")
    val file3 = new File("/home/rwadowski/Downloads/akatsuki.wav")
    val file4 = new File("/home/rwadowski/Downloads/house_of_rising_sun.wav")
    val file5 = new File("/home/rwadowski/Downloads/poranek_kojota.wav")
    val file6 = new File("/home/rwadowski/Downloads/killer2.wav")
    val file7 = new File("/home/rwadowski/Downloads/chlopaki.wav")

    val sample = new File("/home/rwadowski/Downloads/poranek_kojota_sample_1.wav")

    AudioSystem.getAudioFileTypes.foreach(println)

    val l = AudioSystem.getAudioFileTypes()
    println(s"Supported types -> ${l.length}")

    val files = Seq(file1, file2, file3, file4, file5, file6, file7)
    val printer = new FingerPrinter
    var matcher = new Matcher
    val reader = new WaveAudioFileReader
    val map = files.map{ f =>
      val printStart = Instant.now
      val signature = reader.streamFromFile(f)
      val print = printer.process(signature)
      matcher = matcher + print
      val printEnd = Instant.now
      println(s"Duration ${Duration.between(printStart, printEnd).toMillis} ms for ${f.getName}")
      signature.item -> f
    }.toMap

    val sampleFingerPrint = printer.process(reader.streamFromFile(sample))

    val analyzer = new Analyzer
    val start = Instant.now()
    val matches = matcher.process(sampleFingerPrint)
    val result = analyzer.process(matches)
    val end = Instant.now()

    println(s"Duration ${Duration.between(start, end).toMillis} ms")
    val res = result.scores
    println(s"Scores for sample -> ${sample.getName}:")
    println(s"Found for sample -> ${res.maxBy(_._2.rank)}:")
    map.foreach{ case (item, f) =>
      if(res.contains(item)) println(s"File -> ${f.getName} -> ${res(item)}")
    }
    println(s"Timestamps ${result.matchTimestamps}")
  }
}
