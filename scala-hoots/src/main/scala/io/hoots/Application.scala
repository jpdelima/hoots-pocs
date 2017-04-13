package io.hoots

import java.io.File
import java.time.{Duration, Instant}
import java.util.UUID
import javax.sound.sampled.AudioSystem

import io.hoots.`match`.Matcher
import io.hoots.domain.Item
import io.hoots.domain.score.BasicScoreCalculator
import io.hoots.fingerprint.FingerPrinter

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

    val map = Seq(file1, file2, file3, file4, file5, file6, file7).map{f =>
      new Item(f.getName) -> f
    }

    val printer = new FingerPrinter()
    val matcher = new Matcher()
    for((item, f) <- map) {
      val result = printer.exec(f, item)
      matcher.update(result)
    }
    val sampleResult = printer.exec(sample, new Item(sample.getName))

    val calculator = new BasicScoreCalculator
    val start = Instant.now()
    val matchResult = matcher.matchSmaple(sampleResult)
    val scores = calculator.calculate(matchResult)
    val end = Instant.now()

    println(s"Duration ${Duration.between(start, end).toMillis} ms")
    val res = scores.asScala
    println(s"Scores for sample -> ${sample.getName}:")
    println(s"Found for sample -> ${res.maxBy(_._2.getRank)}:")
    for((item, f) <- map) {
      println(s"File -> ${f.getName} -> ${res(item)}")
    }
  }
}
