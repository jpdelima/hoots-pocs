package io.hoots.results

import io.hoots.results.domain.{MatchResult, Matches}

/**
  * Created by rwadowski on 19.04.17.
  */
trait MatchAnalyzer {

  def process(matches: Matches): MatchResult
}
