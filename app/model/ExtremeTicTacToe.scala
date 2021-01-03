package model
import model.Filled.{EMPTY, Filled}

class ExtremeTicTacToe(dim: Int) extends TicTacToeGame {
  val board = new Board(dim)
  private val innerBoards = Array.tabulate(dim, dim)((_, _) => new Board(dim))
  private var listeners: List[GameListener] = List()
  private var prev_symbol: Filled = EMPTY
  private val START = (-1, -1)
  private var prev_loc: (Int, Int) = START

  override def hasWon: Boolean = board.winner.isDefined

  override def hasLost: Boolean = !hasWon && board.isFull

  override def winner: Option[Filled] = board.winner

  override def isTurnValid(player: Player, row: Int, column: Int): Boolean = {
    if (hasWon || hasLost) return false

    if(prev_symbol == player.symbol) return false

    val outer_row = row / dim
    val inner_row = row % dim
    val outer_col = column / dim
    val inner_col = column % dim

    if (!board.inBounds(outer_row, outer_col) || !board.inBounds(inner_row, outer_row)) return false

    if(innerBoards(outer_row)(outer_col).cell(inner_row, inner_col).isFilled) return false

    if(prev_loc != START && (outer_row, outer_col) != prev_loc) return false

    true
  }

  override def takeTurn(player: Player, row: Int, column: Int): Unit = {
    val outer_row = row / dim
    val inner_row = row % dim
    val outer_col = column / dim
    val inner_col = column % dim

    if (!isTurnValid(player, row, column)) throw new IllegalArgumentException("Invalid move")

    innerBoards(outer_row)(outer_col).cell(inner_row, inner_col).value = player.symbol
    if (innerBoards(outer_row)(outer_col).winner.isDefined)
      board.cell(outer_row, outer_col).value = innerBoards(outer_row)(outer_col).winner.get
    prev_loc = (inner_row, inner_col)
    prev_symbol = player.symbol
    listeners.foreach(listener => {
      listener.onUpdate(this)
      if (hasWon) listener.onWin(player)
      else if (hasLost) listener.onLose()
    })

  }

  def fullBoard: Array[Array[Square]] = {
    Array.tabulate(dim*dim, dim*dim)((i, j) => innerBoards(i / dim)(j / dim).cell(i % dim, j % dim))
  }

  override def registerListener(gameListener: GameListener): Unit = listeners = gameListener :: listeners

  override def toString: String = {
    fullBoard
      .map(row => row.mkString("[", "|", "]")).mkString("\n")
  }
}
