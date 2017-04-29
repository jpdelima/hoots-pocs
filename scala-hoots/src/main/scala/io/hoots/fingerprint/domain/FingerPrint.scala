package io.hoots.fingerprint.domain

import javax.sound.sampled.AudioFormat

import io.hoots.input.domain.Item

/**
  * Created by rwadowski on 19.04.17.
  */
case class FingerPrint(item: Item,
                       print: Map[Hash, Seq[Point]],
                       audioFormat: AudioFormat) {

  def +(c: HashedChunk): FingerPrint = {
    if(print.contains(c.hash)) {
      val points = print(c.hash)
      FingerPrint(item, print + (c.hash -> (points :+ c.point)), audioFormat)
    } else {
      FingerPrint(item, print + (c.hash -> Seq(c.point)), audioFormat)
    }
  }
}
