package model

import model.Filled.Filled

trait TicTacToeGame {
  def hasWon: Boolean
  def hasLost: Boolean
  def winner: Option[Filled]
  def isTurnValid(player: Player, row: Int, column: Int): Boolean
  def takeTurn(player: Player, row: Int, column: Int)
  def registerListener(gameListener: GameListener)
}

trait GameListener {
  def onWin(winner: Player)
  def onLose()
  def onUpdate(ticTacToeGame: TicTacToeGame)
}
