package io.hoots.fft.domain

/**
  * Created by rwadowski on 19.04.17.
  */
case class Complex(re: Double, im: Double) {

  override def toString: String = {
    if(im == 0)
      re.toString
    else if(re == 0)
      s"${im}i"
    else if(im < 0)
      s"$re - ${-im}i"
    else
      s"$re + ${im}i"
  }

  lazy val abs: Double = Math.hypot(re, im)

  lazy val phase: Double = Math.atan2(im, re)

  def +(that: Complex): Complex = {
    val real = this.re + that.re
    val imag = this.im + that.im
    Complex(real, imag)
  }

  def -(that: Complex): Complex = {
    val real = this.re - that.re
    val imag = this.im - that.im
    Complex(real, imag)
  }

  def *(that: Complex): Complex = {
    val real = this.re * that.re - this.im * that.im
    val imag = this.re * that.im + this.im * that.re
    Complex(real, imag)
  }

  def /(b: Complex): Complex = {
    this * b.reciprocal
  }

  def *(alpha: Double): Complex = Complex(alpha * re, alpha * im)

  lazy val conjugate: Complex = Complex(re, -im)

  lazy val reciprocal: Complex = {
    val scale = re * re + im * im
    Complex(re / scale, -im / scale)
  }

}
