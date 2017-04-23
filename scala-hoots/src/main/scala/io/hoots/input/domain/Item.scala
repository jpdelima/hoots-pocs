package io.hoots.input.domain

import java.util.UUID

/**
  * Created by rwadowski on 19.04.17.
  */
case class Item(name: String, id: UUID) {

  def this(name: String) = this(name, UUID.randomUUID())
}

object Item {

  def apply(name: String): Item = new Item(name, UUID.randomUUID())
}