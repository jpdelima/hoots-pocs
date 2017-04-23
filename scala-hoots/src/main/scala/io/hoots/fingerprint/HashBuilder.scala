package io.hoots.fingerprint

import io.hoots.fingerprint.domain.Hash

/**
  * Created by rwadowski on 23.04.17.
  */
trait HashBuilder {

  def hash(points: Seq[Long]): Hash
}
