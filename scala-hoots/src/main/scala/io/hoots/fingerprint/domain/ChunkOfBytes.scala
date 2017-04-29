package io.hoots.fingerprint.domain

import io.hoots.fft.domain.Complex
import io.hoots.input.domain.Item

/**
  * Created by rwadowski on 19.04.17.
  */
case class ChunkOfBytes(number: ChunkNumber,
                        size: ChunkSize,
                        item: Item,
                        data: List[Byte] = List.empty) {

  lazy val chunkByte: Int = number.value * size.value
}

case class ChunkOfComplex(number: ChunkNumber,
                          size: ChunkSize,
                          item: Item,
                          data: List[Complex])

case class ChunkSize(value: Int)

case class ChunkNumber(value: Int) extends Ordered[ChunkNumber] {
  override def compare(that: ChunkNumber): Int = {
    this.value - that.value
  }
}

case class HashedChunk(number: ChunkNumber,
                       chunkSize: ChunkSize,
                       hash: Hash,
                       item: Item) {

  lazy val point: Point = Point(item, number)
}
