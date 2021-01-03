package model

import org.scalatest.matchers.should.Matchers._
import org.scalatest.flatspec.AnyFlatSpec


class GameStoreTest extends AnyFlatSpec{

  "A Game Store" should "be able to add and retrieve a new game" in {
    val gameStore = new GameStore
    val game = new BasicTicTacToe(3)
    gameStore.addGame("fun", game)

    gameStore.getGame("fun") should contain (game)
  }

  "A Game Store" should "be able to add and remove a game by name" in {
    val gameStore = new GameStore
    val game = new BasicTicTacToe(3)
    gameStore.addGame("fun", game)
    gameStore.removeGame("fun")

    gameStore.getGame("fun") should not contain (game)
  }

  "A Game Store" should "be able to add and remove a game by object" in {
    val gameStore = new GameStore
    val game = new BasicTicTacToe(3)
    gameStore.addGame("fun", game)
    gameStore.removeGame(game)

    gameStore.getGame("fun") should not contain (game)
    gameStore.contains("fun") should be (false)
  }

  "A Game Store" should "register players" in {
    val gameStore = new GameStore
    val game = new BasicTicTacToe(3)
    gameStore.addGame("fun", game)
    val px = gameStore.registerPlayer("alec", game)
    val po = gameStore.registerPlayer("timbob", game)

    px.symbol should be (Filled.X)
    px.name should be ("alec")

    po.symbol should be (Filled.O)
    po.name should be ("timbob")

  }

}
