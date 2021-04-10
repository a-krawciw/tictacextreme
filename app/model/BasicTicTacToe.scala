package model
import model.Filled.{EMPTY, Filled}

class BasicTicTacToe(dim: Int = 3) extends TicTacToeGame {
  val board = new Board(dim)
  private var prev_symbol =  EMPTY
  private var listeners: List[GameListener] = List()


  override def hasLost: Boolean = !hasWon && board.isFull

  override def hasWon: Boolean = board.winner.isDefined

  override def winner: Option[Filled] = board.winner

  override def isTurnValid(player: Player, row: Int, column: Int): Boolean = {
    if (hasWon || hasLost) return false

    if (!board.inBounds(row, column)) return false

    if (board.cell(row, column).isFilled) return false

    if(prev_symbol == player.symbol) return false

    true
  }

  override def takeTurn(player: Player, row: Int, column: Int): Unit = {
    board.cell(row, column).value = player.symbol
    prev_symbol = player.symbol
    listeners.foreach(listener => {
      listener.onUpdate(this)
      if (hasWon) listener.onWin(player)
      else if (hasLost) listener.onLose()
    })

  }

  override def toString: String = board.toString


  override def registerListener(gameListener: GameListener): Unit = listeners = gameListener :: listeners

  override def isComplete: Boolean = hasLost || hasWon
}
