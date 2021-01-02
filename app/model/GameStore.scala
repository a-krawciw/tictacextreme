package model

import javax.inject.Singleton

@Singleton
class GameStore {
  def contains(name: String): Boolean = gameMap.contains(name)

  private var gameMap: Map[String, TicTacToeGame] = Map()
  private var playerMap: Map[TicTacToeGame, (Player, Player)] = Map()

  def addGame(name: String): Boolean = {
    if (gameMap.contains(name))
      return false
    gameMap = gameMap + (name -> new BasicTicTacToe)
    true
  }

  def getGame(name: String): Option[TicTacToeGame] = {
    gameMap.get(name)
  }

  def registerPlayer(playerName: String, ticTacToeGame: TicTacToeGame): Player =
    if (playerMap.contains(ticTacToeGame)) {
      if(playerMap(ticTacToeGame)._2.symbol == Filled.EMPTY){
        val player2 = new Player(playerName, Filled.O)
        val player1 = playerMap(ticTacToeGame)._1
        playerMap = playerMap + (ticTacToeGame -> (player1, player2))
        return player2
      }
      new Player("", Filled.EMPTY)
    } else {
      val player1 = new Player(playerName, Filled.X)
      val player2 = new Player("", Filled.EMPTY)
      playerMap = playerMap + (ticTacToeGame -> (player1, player2))
      player1
    }
}
