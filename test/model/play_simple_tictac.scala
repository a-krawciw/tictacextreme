package model

import akka.protobufv3.internal.TextFormat.ParseException

object play_simple_tictac {

  private class SimpleListener extends GameListener{
    var active = true
    override def onWin(winner: Player): Unit = {
      println("Congratulations " + winner.name + " you win")
      active = false
    }

    override def onLose(): Unit = {
      println("This game is a draw")
      active = false
    }

    override def onUpdate(ticTacToeGame: TicTacToeGame): Unit = println(ticTacToeGame)
  }

  def main(args: Array[String]): Unit = {
    val game: TicTacToeGame = new BasicTicTacToe
    val player1 = new Player("Player 1", Filled.X)
    val player2 = new Player("Player 2", Filled.O)

    val gameManager = new SimpleListener

    game.registerListener(gameManager)

    var activePlayer = player1
    println(game)
    while (gameManager.active) {
      println(activePlayer.name + " Please enter row,column coordinates for your move")

      try {
        val coords = scala.io.StdIn.readf("{0},{1}").map(coord => coord.toString.toInt)
        if (game.isTurnValid(activePlayer, coords(0), coords(1))) {
          game.takeTurn(activePlayer, coords(0), coords(1))
          activePlayer = if (activePlayer == player1) player2 else player1
        } else {
          println(activePlayer.name + " Please try again. That move was invalid")
        }
      } catch {
        case _: java.text.ParseException => println(activePlayer.name + " Please try again. That move was invalid")
      }
    }
  }

}
