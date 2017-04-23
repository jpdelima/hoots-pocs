package io.hoots.fft

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D
import io.hoots.fft.domain.Complex

/**
  * Created by rwadowski on 20.04.17.
  */
class JTransformFFT extends FFT {

  def transform(data: Array[Byte]): Array[Complex] = {
    val len = data.length
    val buffer = Array.ofDim[Float](2 * len)
    var i = 0
    while(i < len) {
      buffer(i) = data(i).toFloat
      buffer(2 * i + 1) = 0f
      i = i + 1
    }
    val transform = new FloatFFT_1D(len)
    transform.realForwardFull(buffer)
    val result = Array.ofDim[Complex](len)
    i = 0
    while(i < len) {
      result(i) = Complex(buffer(2 * i), buffer(2 * i + 1))
      i = i + 1
    }
    result
  }
}
