package io.hoots.fingerprint

import io.hoots.fingerprint.domain.Hash

/**
  * Created by rwadowski on 23.04.17.
  */
class DefaultHashBuilder(fuzzFactor: Long) extends HashBuilder {

  override def hash(points: Seq[Long]): Hash = {
    if(points.size < 4) {
      throw new RuntimeException("Not enough points to calculate hash value")
    }
    val p1 = points.head
    val p2 = points(1)
    val p3 = points(2)
    val p4 = points(3)
    val v = (p4 - (p4 % fuzzFactor)) * 100000000 + (p3 - (p3 % fuzzFactor)) * 100000 + (p2 - (p2 % fuzzFactor)) * 100 + (p1 - (p1 % fuzzFactor))
    Hash(v)
  }
}
