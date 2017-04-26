package io.hoots.fingerprint.domain

import scala.annotation.tailrec

/**
  * Created by rwadowski on 25.04.17.
  */
case class PrinterConfig(upperLimit: UpperLimit,
                         lowerLimit: LowerLimit,
                         chunkSize: ChunkSize,
                         fuzzFactor: FuzzFactor) {

  lazy val frequencies: Range = lowerLimit.value until upperLimit.value

  def ranges(length: Int): Seq[Int] = {
    @tailrec
    def loop(i: Int, result: Seq[Int]): Seq[Int] = {
      if(0 == i) {
        loop(1, Seq(lowerLimit.value))
      } else if(i < length - 1) {
        loop(i + 1, result :+ ((i + 1) * lowerLimit.value))
      } else if(i == length - 1) {
        val nextCandidate = (i + 1) * lowerLimit.value
        val next = if(nextCandidate > upperLimit.value) nextCandidate else upperLimit.value
        loop(i + 1, result :+ next)
      } else {
        result
      }
    }
    loop(0, Seq.empty)
  }
}

trait Limit {
  def value: Int
}

case class UpperLimit(override val value: Int) extends Limit
case class LowerLimit(override val value: Int) extends Limit

case class FuzzFactor(value: Int)
