package model

import model.Filled.{EMPTY, Filled}



class Board (dim: Int){

  private val _board: Array[Array[Square]] = Array.tabulate(dim, dim)((_, _) => new Square)

  def board: Array[Array[Square]] = _board

  def inBounds(row: Int, column: Int): Boolean = row >= 0 && column >= 0 && column < dim && row < dim

  def cell(r: Int, c: Int): Square = board(r)(c)

  def rowValid(array: Array[Square]): Boolean = array.forall(_ == array.head) && array.head.value != Filled.EMPTY

  def getColumn(n: Int): Array[Square] = Array.tabulate(dim)(i => cell(i, n))

  def getMajorTrace: Array[Square] =  Array.tabulate(dim)(n => cell(n, n))

  def getMinorTrace: Array[Square] = Array.tabulate(dim)(n => cell(n, dim - 1 - n))

  def winner: Option[Filled] = {
    for (row <- board)  {
      if (rowValid(row)){
        return Some(row(0).value)
      }
    }

    for (i <- 0 until dim){
      val column = getColumn(i)
      if (rowValid(column)){
        return Some(column(0).value)
      }
    }

    val minor = getMinorTrace
    if (rowValid(minor)){
      return Some(minor(0).value)
    }

    val major = getMajorTrace
    if (rowValid(major)){
      return Some(major(0).value)
    }
    None
  }

  def isFull: Boolean = _board.flatten.forall(s => s.isFilled)

  override def toString: String = _board.map(row => row.mkString("[", ",", "]")).mkString("\n")
}


object Filled extends Enumeration {
  type Filled = Val

  def fromString(str: String): Filled ={
    str match {
      case "X" => X
      case "O" => O
      case _ => EMPTY
    }
  }

  def fromString(ch: Char): Filled ={
    fromString(ch + "")
  }

  protected case class Val(displayString: String) extends super.Val {
    override def toString: String = displayString
  }
  val EMPTY: Filled = Val("-")
  val X: Filled = Val("X")
  val O: Filled = Val("O")
}

class Square(private var _value: Filled = Filled.EMPTY){
  def value: Filled = _value
  def isFilled: Boolean = value != Filled.EMPTY
  def value_= (newVal: Filled): Unit = {
    if (isFilled && newVal != _value)
      throw new IllegalArgumentException("Square already filled")
    _value = newVal
  }

  override def toString: String = value.toString

  override def equals(obj: Any): Boolean = {
      obj match {
        case s: Square => s.value == this.value
        case _ => false
      }
  }

}