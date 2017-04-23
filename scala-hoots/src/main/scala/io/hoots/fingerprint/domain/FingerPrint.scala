package io.hoots.fingerprint.domain

import javax.sound.sampled.AudioFormat

import io.hoots.input.domain.Item

/**
  * Created by rwadowski on 19.04.17.
  */
case class FingerPrint(item: Item,
                       print: Map[Hash, Seq[Point]],
                       audioFormat: AudioFormat)
