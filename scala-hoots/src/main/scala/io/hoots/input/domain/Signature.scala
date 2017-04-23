package io.hoots.input.domain

import java.io.ByteArrayOutputStream
import javax.sound.sampled.AudioFormat

/**
  * Created by rwadowski on 19.04.17.
  */
case class Signature(item: Item,
                     byteArrayOutputStream: ByteArrayOutputStream,
                     audioFormat: AudioFormat)
