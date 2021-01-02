package model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec

class BoardTest extends AnyFlatSpec {

  "A Board" must "Contain n by n Squares" in {
    val board = new Board(3)
    assertResult(3)( board.board.length)
    assertResult(3)( board.board(0).length)
  }

  "A Board" must "be able to extract columns" in {
    val board = BoardBuilder.fromString("OOO;XO-;---")
    val column = board.getColumn(1)

    column should equal (Array(new Square(Filled.O), new Square(Filled.O), new Square(Filled.EMPTY)))
  }

  "A Board" must "be able to extract major trace" in {
    val board = BoardBuilder.fromString("OOO;XO-;--X")
    val column = board.getMajorTrace

    column should equal (Array(new Square(Filled.O), new Square(Filled.O), new Square(Filled.X)))
  }

  "A Board" must "be able to extract minor trace" in {
    val board = BoardBuilder.fromString("O-X;XO-;O-X")
    val column = board.getMinorTrace

    column should equal (Array(new Square(Filled.X), new Square(Filled.O), new Square(Filled.O)))
  }



  "An empty board" must "Have no winner" in {
    val board = new Board(3)
    board.winner should be (Option.empty)
  }

  "An empty board" must "Only contain empty cells" in {
    val board = new Board(3)

    board.board.flatten.map(square => square.value) should contain only (Filled.EMPTY)
  }

  "An empty board" must "have its cells set to X and Y" in {
    val board = new Board(3)
    board.board(2)(0).value = Filled.X

    board.board.flatten.map(square => square.value) should contain (Filled.X)
  }

  "A board with row of X" should "win with X" in {
    val board = BoardBuilder.fromString("XXX;XO-;---")
    board.winner should contain (Filled.X)
  }

  "A board with row of O" should "win with O" in {
    val board = BoardBuilder.fromString("OOO;XO-;---")
    board.winner should contain (Filled.O)
  }


  "A board with column of X" should "win with X" in {
    val board = BoardBuilder.fromString("XOX;XO-;X--")
    board.winner should contain (Filled.X)
  }

  "A board with column of O" should "win with O" in {
    val board = BoardBuilder.fromString("OOX;XO-;-O-")
    board.winner should contain (Filled.O)
  }

  "A board with major diagonal of X" should "win with X" in {
    val board = BoardBuilder.fromString("XOX;-X-;--X")
    board.winner should contain (Filled.X)
  }

  "A board with a major diagonal of O" should "win with O" in {
    val board = BoardBuilder.fromString("OOX;XO-;--O")
    board.winner should contain (Filled.O)
  }

  "A board with minor diagonal of X" should "win with X" in {
    val board = BoardBuilder.fromString("--X;-X-;X--")
    board.winner should contain (Filled.X)
  }

  "A board with a minor diagonal of O" should "win with O" in {
    val board = BoardBuilder.fromString("--O;-O-;O--")
    board.winner should contain (Filled.O)
  }

  "A board" should "have no winner for a single play" in {
    val board = BoardBuilder.fromString("X--;---;---")
    board.winner should be (None)
  }

  "A row" should "be valid if it contains the same items that are not Empty" in {
    val b = new Board(3)
    val row = Array(new Square(Filled.O), new Square, new Square)
    b.rowValid(row) should be (false)
  }



}
