package io.hoots.results
import io.hoots.fingerprint.domain.Point
import io.hoots.input.domain.Item
import io.hoots.results.domain.{MatchResult, Matches, Score}

/**
  * Created by rwadowski on 19.04.17.
  */
class Analyzer extends MatchAnalyzer {

  override def process(m: Matches): MatchResult = {
    val matches = m.list
    var scores: Map[Item, Score] = Map.empty
    var matchPoints: Map[Item, Point] = Map.empty
    for(hashMatch <- matches) {
      val groupedPoints = hashMatch.matchedPoints.groupBy{_.item}
      val sampleOffsets = normalize(hashMatch.samplePoints)
      for((item, points) <- groupedPoints) {
        val pointOffsets = normalize(points)
        val intersection = pointOffsets.intersect(sampleOffsets)
        matchPoints = matchPoints + (item -> points.minBy(_.chunk.number.value))
        if(scores.contains(item)) {
          scores = scores + (item -> (scores(item) + intersection.size))
        } else {
          scores = scores + (item -> Score(intersection.size))
        }
      }
    }
    MatchResult(scores, matchPoints, m.audioFormat)
  }

  private def normalize(offsets: Seq[Point]): Seq[Int] = {
    val offsetValues = offsets.map{_.chunk.number.value}
    val min = offsetValues.min
    offsetValues.map{_ - min}
  }
}
