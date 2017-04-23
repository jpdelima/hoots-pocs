package io.hoots.results.domain

import io.hoots.fingerprint.domain.{FingerPrint, Hash, HashMatch, Point}

/**
  * Created by rwadowski on 19.04.17.
  */
class Matcher(hashesMap: Map[Hash, Seq[Point]] = Map.empty) {

  private lazy val hashes: Set[Hash] = hashesMap.keys.toSet

//  def +(fp: FingerPrint): Matcher = {
//
//    val m = collection.mutable.Map(hashesMap.toSeq: _*)
//    for((hash, newPoints) <- fp.print) {
//      if(m.contains(hash)) {
//        val currentPoints = m(hash)
//        m += hash -> (currentPoints ++ newPoints)
//      } else {
//        m += hash -> newPoints
//      }
//    }
//    new Matcher(m.toMap)
//  }

  def +(fp: FingerPrint): Matcher = {
    var map = hashesMap
    for((hash, newPoints) <- fp.print) {
      if(map.contains(hash)) {
        val currentPoints = map(hash)
        map = map + (hash -> (currentPoints ++ newPoints))
      } else {
        map = map + (hash -> newPoints)
      }
    }
    new Matcher(map)
  }

  def process(fp: FingerPrint): Matches = {
    val sample = fp.print
    val matched = hashes.intersect(sample.keys.toSet)

    val matchList = matched.map{h =>
      HashMatch(h, sample(h), hashesMap(h))
    }
    Matches(matchList.toSeq, fp.audioFormat)
  }
}
