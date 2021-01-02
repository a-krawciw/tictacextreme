package model

object BoardBuilder {

  def fromString(boardString: String): Board = {
    val rows = boardString.split(";")
    val board = new Board(rows.length)
    boardString.replaceAll(";", "")
      .zip(board.board.flatten.toIterable)
      .foreach(tup => tup._2.value = Filled.fromString(tup._1))
    board
  }

}
