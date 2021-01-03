package model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec

class ExtremeTicTacToeTest extends AnyFlatSpec {
  val px = new Player("", Filled.X)
  val po = new Player("", Filled.O)

  "An empty game" should "accept any move on the board" in {
    val game = new ExtremeTicTacToe(3)

    game.isTurnValid(px, 0, 0) should be (true)
    game.isTurnValid(po, 0, 0) should be (true)
    game.isTurnValid(px, 0, 8) should be (true)
    game.isTurnValid(po, 0, 8) should be (true)
    game.isTurnValid(px, 8, 0) should be (true)
    game.isTurnValid(po, 8, 0) should be (true)
    game.isTurnValid(px, 8, 8) should be (true)
    game.isTurnValid(po, 8, 8) should be (true)
  }

  "An empty game" should "have no winner" in {
    val game = new ExtremeTicTacToe(3)
    game.winner should be (Option.empty)
  }
  
  "A game" should "not accept any move outside the board" in {
    val game = new ExtremeTicTacToe(3)

    game.isTurnValid(px, -1, -1) should be (false)
    game.isTurnValid(po, -1, -1) should be (false)
    game.isTurnValid(px, -1, 9) should be (false)
    game.isTurnValid(po, -1, 9) should be (false)
    game.isTurnValid(px, 9, -1) should be (false)
    game.isTurnValid(po, 9, -1) should be (false)
    game.isTurnValid(px, 9, 9) should be (false)
    game.isTurnValid(po, 9, 9) should be (false)
  }

  "A game" should "not accept two moves from the same player" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(px, 0, 0)

    game.isTurnValid(px, 0, 2) should be (false)
  }

  "A game" should "not accept two moves on the same square" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(px, 0, 0)

    game.isTurnValid(po, 0, 0) should be (false)
  }

  "A game" should "accept a move in the correct major box" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(px, 1, 1)

    game.isTurnValid(po, 3, 3) should be (true)
  }

  "A game" should "not accept a move in the incorrect major box" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(px, 1, 1)

    game.isTurnValid(po, 1, 3) should be (false)
  }

  "A game" should "accept moves after a small cell has been won" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(px, 1, 1)
    game.takeTurn(po, 3, 3)
    game.takeTurn(px, 2, 2)
    game.takeTurn(po, 6, 6)
    game.takeTurn(px, 0, 0)

    game.isTurnValid(po, 0, 1) should be (true)
    game.takeTurn(po, 0, 1)
    game.innerBoards(0)(0).winner should contain (px.symbol)
    game.board.cell(0, 0).value should equal (px.symbol)

  }

  "A subgame" should "retain the same winner if there are two valid wins" in {
    val game = new ExtremeTicTacToe(3)

    game.takeTurn(po, 0, 0)
    game.takeTurn(px, 1, 1)
    game.takeTurn(po, 3, 3)
    game.takeTurn(px, 0, 1)
    game.takeTurn(po, 0, 3)
    game.takeTurn(px, 2, 1)

    game.innerBoards(0)(0).winner should contain (px.symbol)
    game.board.cell(0, 0).value should equal (px.symbol)

    game.takeTurn(po, 8, 4)
    game.takeTurn(px, 6, 3)
    game.takeTurn(po, 1, 0)
    game.takeTurn(px, 3, 0)
    game.takeTurn(po, 2, 0)

    game.board.cell(0, 0).value should equal (px.symbol)
  }


}
