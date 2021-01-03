package controllers

import akka.actor.ActorSystem
import akka.stream.Materializer
import model._
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.libs.streams.ActorFlow
import play.api.mvc.{AbstractController, ControllerComponents, WebSocket}

import java.text.MessageFormat
import javax.inject.Inject

class WebSocketController @Inject()(cc: ControllerComponents)
                                   (implicit gameStore: GameStore, messagesApi: MessagesApi, system: ActorSystem, mat: Materializer) extends AbstractController(cc) {

  def socket(gameName: String) = WebSocket.accept[String, String] { request =>
    val game = gameStore.getGame(gameName).get
    ActorFlow.actorRef { out =>
      MyWebSocketActor.props(out, game, gameStore.registerPlayer(request.cookies.get("playerName").get.value, game))
    }
  }


}


import akka.actor._

object MyWebSocketActor {
  def props(out: ActorRef, ticTacToeGame: TicTacToeGame, player: Player)(implicit gameStore: GameStore) = Props(new MyWebSocketActor(out, ticTacToeGame, player))
}


class MyWebSocketActor(out: ActorRef, ticTacToeGame: TicTacToeGame, player: Player)(implicit gameStore: GameStore) extends Actor with GameListener {
  ticTacToeGame.registerListener(this)

  private val messageFormat = new MessageFormat("{0},{1}")

  override def postStop(): Unit = gameStore.removeGame(ticTacToeGame)

  def receive = {
    case msg: String if msg != "ping" =>
        try {
          val parts = messageFormat.parse(msg)
          if (ticTacToeGame.isTurnValid(player, parts(0).asInstanceOf[String].toInt, parts(1).asInstanceOf[String].toInt)) {
            ticTacToeGame.takeTurn(player, parts(0).asInstanceOf[String].toInt, parts(1).asInstanceOf[String].toInt)
          } else {
            println("Invalid move")
            out ! "Invalid move!"
          }
        } catch {
          case e: java.text.ParseException => {
            e.printStackTrace()
            out ! "Parse error"
          }
          case _: NumberFormatException => {
            out ! "Parse error"
          }
        }
  }

  override def onWin(winner: Player): Unit = out ! (winner.name + " Won!")

  override def onLose(): Unit = out ! "This game ends in a draw"

  override def onUpdate(ticTacToeGame: TicTacToeGame): Unit = out ! ""
}