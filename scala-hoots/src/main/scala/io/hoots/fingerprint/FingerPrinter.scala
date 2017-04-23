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
  val fuzFactor = 3
  private val hashBuilder = new DefaultHashBuilder(fuzFactor)

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

      val hash = hashBuilder.hash(Seq(points(t)(0), points(t)(1), points(t)(2), points(t)(3)))
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

  private def indexForFrequency(frequency: Int): Int = {
    var i = 0
    while(range(i) < frequency) {
      i = i + 1
    }
    i
  }

}
