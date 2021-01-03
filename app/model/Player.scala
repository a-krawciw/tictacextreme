package model

import model.Filled.Filled

case class Player (name: String, symbol: Filled) {
  val wins = 0
  val losses = 0

  override def toString: String = name + " is playing as " + symbol
}
