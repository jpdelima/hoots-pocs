package io.hoots.fingerprint.domain

/**
  * Created by rwadowski on 19.04.17.
  */
case class Hash(value: Long)

case class HashMatch(hash: Hash, samplePoints: Seq[Point], matchedPoints: Seq[Point]) {

  def +(p: Seq[Point]): HashMatch = {
    HashMatch(hash, samplePoints, matchedPoints ++ p)
  }
}