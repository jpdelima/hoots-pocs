package io.hoots.fft

import io.hoots.fft.domain.Complex

/**
  * Created by rwadowski on 23.04.17.
  */
trait FFT {

  def transform(data: Array[Byte]): Array[Complex]
}
