package io.hoots.fft

/**
  * Created by rwadowski on 26.04.17.
  */
sealed trait FFTFactory {

  def create: FFT
}

class DefaultFFTFactory extends FFTFactory {

  override def create: FFT = new JTransformFFT
}
