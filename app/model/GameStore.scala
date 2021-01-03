package model

import javax.inject.Singleton

@Singleton
class GameStore {

  def contains(name: String): Boolean = gameMap.contains(name)

  private var gameMap: Map[String, TicTacToeGame] = Map()
  private var playerMap: Map[TicTacToeGame, PlayersSet] = Map()
  private val EMPTY: Player = Player("No one ", Filled.EMPTY)

  def addGame(name: String, game: TicTacToeGame): Boolean = {
    if (gameMap.contains(name))
      return false
    gameMap = gameMap + (name -> game)
    playerMap = playerMap + (game -> new PlayersSet())
    true
  }

  def getGame(name: String): Option[TicTacToeGame] = {
    gameMap.get(name)
  }

  def registerPlayer(playerName: String, ticTacToeGame: TicTacToeGame): Player =
    if (playerMap.contains(ticTacToeGame)) {
      val players = playerMap(ticTacToeGame)
      players.addPlayer(playerName)
    } else {
      EMPTY
    }

  def unRegisterPlayer(player: Player, ticTacToeGame: TicTacToeGame): Unit =
    if(playerMap.contains(ticTacToeGame)) {
      playerMap(ticTacToeGame).removePlayer(player)
    }

  def playersRemaining(ticTacToeGame: TicTacToeGame): Int = getPlayers(ticTacToeGame).count

  def getPlayers(ticTacToeGame: TicTacToeGame): PlayersSet = playerMap.getOrElse(ticTacToeGame, new PlayersSet())

  def removeGame(gameName: String) = {
    gameMap = gameMap.-(gameName)
  }

  def removeGame(game: TicTacToeGame) = gameMap = gameMap.filter(tuple1 => game != tuple1._2)

  class PlayersSet(var x: Player = EMPTY, var o: Player = EMPTY){
    def removePlayer(player: Player): Unit = {
      if (x == player) x = EMPTY
      if (o == player) o = EMPTY
    }

    def addPlayer(playerName: String): Player =
      if (x == EMPTY) {
        x = Player(playerName, Filled.X)
        x
      } else if (o == EMPTY) {
        o = Player(playerName, Filled.O)
        o
      } else
        Player(playerName, Filled.EMPTY)

    implicit def bool2int(b:Boolean) = if (b) 1 else 0
    def count: Int = ((x != EMPTY):Int) + (o != EMPTY):Int
  }
}
