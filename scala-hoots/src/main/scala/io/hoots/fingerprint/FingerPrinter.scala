package io.hoots.fingerprint

import java.io.ByteArrayOutputStream

import io.hoots.fft.{FFT, JTransformFFT}
import io.hoots.fft.domain.Complex
import io.hoots.fingerprint.domain._
import io.hoots.input.domain.{Item, Signature}

import scala.collection.mutable


/**
  * Created by rwadowski on 19.04.17.
  */
class FingerPrinter(fft: FFT = new JTransformFFT) {

  val upperLimit = 300
  val lowerLimit = 40
  val chunkSize = ChunkSize(4096)
  val FUZ_FACTOR = 3

  val range = Seq(lowerLimit, 80, 120, 180, upperLimit)
  val frequencies: Range = lowerLimit until upperLimit

  def process(signature: Signature): FingerPrint = {
    val item = signature.item
    val s = spectrum(signature.byteArrayOutputStream, item)
    FingerPrint(item, s, signature.audioFormat)
  }

  private def spectrum(array: ByteArrayOutputStream, item: Item): Map[Hash, Seq[Point]] = {
    val audio = array.toByteArray
    val arrays = audio.sliding(chunkSize.value, chunkSize.value).toArray
    val results = arrays.map{fft.transform}
    val points = keyPoints(results, item)
    points
  }

  private def keyPoints(results: Array[Array[Complex]], item: Item): Map[Hash, Seq[Point]] = {
    val size = results.length
    val highscores = Array.fill[Double](size, range.size){0}
    val points = Array.fill[Long](size, range.size){0}
    val pointsMap: scala.collection.mutable.Map[Hash, mutable.ListBuffer[Point]] = mutable.Map.empty
    var t = 0
    while(t < size) {
      var frequency = lowerLimit
      while(frequency < upperLimit) {
        val magnitude = Math.log(results(t)(frequency).abs + 1)
        val index = indexForFrequency(frequency)

        if(magnitude > highscores(t)(index)) {
          highscores(t)(index) = magnitude
          points(t)(index) = frequency
        }
        frequency = frequency + 1
      }

      val h = hashForPoints(points(t)(0), points(t)(1), points(t)(2), points(t)(3))
      val hash = Hash(h)
      val point = Point(item, Chunk(ChunkNumber(t), chunkSize))
      if(pointsMap.contains(hash)) {
        val hashPoints = pointsMap(hash)
        hashPoints += point
      } else {
        pointsMap += hash -> mutable.ListBuffer(point)
      }
      t = t + 1
    }
    pointsMap.map{case (hash, buffer) => hash -> buffer.toList}.toMap
  }

  private def hashForPoints(p1: Long, p2: Long, p3: Long, p4: Long): Long = {
    (p4 - (p4 % FUZ_FACTOR)) * 100000000 + (p3 - (p3 % FUZ_FACTOR)) * 100000 + (p2 - (p2 % FUZ_FACTOR)) * 100 + (p1 - (p1 % FUZ_FACTOR))
  }

  private def indexForFrequency(frequency: Int): Int = {
    var i = 0
    while(range(i) < frequency) {
      i = i + 1
    }
    i
  }

}
