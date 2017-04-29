package io.hoots.fft

import io.hoots.fft.domain.Complex
import io.hoots.fingerprint.domain.{ChunkOfBytes, ChunkOfComplex}

/**
  * Created by rwadowski on 23.04.17.
  */
trait FFT {

  def transform(data: List[Byte]): List[Complex]

  def transform(data: ChunkOfBytes): ChunkOfComplex
}
