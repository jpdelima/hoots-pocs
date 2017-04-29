package io.hoots.fingerprint

import io.hoots.fingerprint.domain.{ChunkOfComplex, HashedChunk, PrinterConfig}

/**
  * Created by rwadowski on 28.04.17.
  */
class ChunkPrinter(hashBuilder: HashBuilder,
                   config: PrinterConfig) {

  //FixMe hardcoded
  private val range: Seq[Int] = config.ranges(5)

  def print(chunk: ChunkOfComplex): HashedChunk = {
    var frequency = config.lowerLimit.value
    val scores = Array.ofDim[Double](range.length)
    val points = Array.ofDim[Long](range.length)
    while(frequency < config.upperLimit.value) {
      val magnitude = Math.log(chunk.data(frequency).abs + 1)
      val index = indexForFrequency(frequency)
      if(magnitude > scores(index)) {
        scores(index) = magnitude
        points(index) = frequency
      }
      frequency = frequency + 1
    }
    //FixMe - hardcoded
    val hash = hashBuilder.hash(points.toSeq.take(4))
    HashedChunk(chunk.number, chunk.size, hash, chunk.item)
  }

  private def indexForFrequency(frequency: Int): Int = {
    var i = 0
    while(range(i) < frequency) {
      i = i + 1
    }
    i
  }
}
