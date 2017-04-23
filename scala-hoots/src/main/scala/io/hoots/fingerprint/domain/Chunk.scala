package io.hoots.fingerprint.domain

/**
  * Created by rwadowski on 19.04.17.
  */
case class Chunk(number: ChunkNumber, size: ChunkSize) {

  lazy val chunkByte: Int = number.value * size.value
}

case class ChunkSize(value: Int)

case class ChunkNumber(value: Int) extends Ordered[ChunkNumber] {
  override def compare(that: ChunkNumber): Int = {
    this.value - that.value
  }
}
