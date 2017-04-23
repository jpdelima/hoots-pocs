package io.hoots.results.domain

import javax.sound.sampled.AudioFormat

import io.hoots.fingerprint.domain.{HashMatch, Point}
import io.hoots.input.domain.Item

/**
  * Created by rwadowski on 19.04.17.
  */
case class Matches(list: Seq[HashMatch],
                   audioFormat: AudioFormat) {

}


case class MatchResult(scores: Map[Item, Score],
                       matchPoints: Map[Item, Point],
                       audioFormat: AudioFormat) {

  lazy val matchTimestamps: Map[Item, Float] = {
    var map: Map[Item, Float] = Map.empty
    val sampleSizeInBytes = audioFormat.getSampleSizeInBits / 8
    for((item, point) <- matchPoints) {
      val chunk = point.chunk
      val ts = chunk.chunkByte / (audioFormat.getSampleRate * sampleSizeInBytes)
      map = map + (item -> ts)
    }
    map
  }
}