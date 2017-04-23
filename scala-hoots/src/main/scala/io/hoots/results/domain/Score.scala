package io.hoots.results.domain

/**
  * Created by rwadowski on 19.04.17.
  */
case class Score(rank: Int) {

  def +(v: Int): Score = Score(rank + v)
}
