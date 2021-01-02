package model


import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers.convertToAnyMustWrapper

class SquareTest extends AnyFlatSpec {

  "A Square with no parameters" must "be empty" in {
    val s = new Square
    s.value should be (Filled.EMPTY)
  }

  "A Square with parameters" must "match that parameter" in {
    val s = new Square(Filled.X)
    s.value should be (Filled.X)
  }

  "A Square " must " only update once" in {
    val s = new Square
    s.value = Filled.X
    s.value should be (Filled.X)

    an [IllegalArgumentException] should be thrownBy(s.value = Filled.O)
  }

}
