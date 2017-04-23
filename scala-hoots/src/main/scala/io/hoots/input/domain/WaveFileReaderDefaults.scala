package io.hoots.input.domain

import javax.sound.sampled.AudioFormat

/**
  * Created by rwadowski on 19.04.17.
  */
object WaveFileReaderDefaults {

  val sampleRate: Int = 44100
  val sampleSizeInBits: Int = 8
  val channels: Int = 1 //mono
  val signed: Boolean = true
  val bigEndian: Boolean = true
  val outputFormat = new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian)
  val BUFFER_SIZE: Int = 1024

  val decodedEncoding: AudioFormat.Encoding = AudioFormat.Encoding.PCM_SIGNED
  val decodedSampleSizeInBits: Int = 16
  val decodedBigEndian: Boolean = false

}
