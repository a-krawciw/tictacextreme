package controllers

import model.{BasicTicTacToe, GameStore}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.Constraints
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Cookie, Request, RequestHeader}
import services.{Counter, CreateData, CreateDataConstraints}

import javax.inject.Inject

class GamesManager @Inject() (cc: ControllerComponents)
                             (implicit gameStore: GameStore, messagesApi: MessagesApi) extends AbstractController(cc) {



  val gameForm: Form[CreateData] = Form(
    mapping(
      "Game Name" -> text.verifying(Constraints.nonEmpty(errorMessage="A game name is required")),
      "Player Name" -> text
    )(CreateData.apply)(CreateData.unapply))


  val createGame: Action[AnyContent] = Action { implicit request =>

    val filledForm = gameForm.bindFromRequest

    filledForm.fold(
      badForm => BadRequest(views.html.createGame(badForm)(messagesApi.preferred(request), request)),
      formData => {
        if (gameStore.addGame(formData.gameName))
          Redirect(routes.GamesManager.showGame(formData.gameName)).withCookies(Cookie("playerName", formData.playerName))
        else
          BadRequest(views.html.createGame(filledForm.withError("Game Name", "This game name is already in use."))(messagesApi.preferred(request), request))
      })
  }

  def showGame(name: String) = Action { implicit request =>
    if (gameStore.contains(name)) {
      if(request.cookies.get("playerName").isDefined)
        Ok(views.html.simpleGame(gameStore.getGame(name).get))
      else
        Redirect(routes.GamesManager.createGame()).withHeaders(("Game Name", name))
    } else {
      Redirect(routes.HomeController.index())
    }
  }

  def getBoard(name: String) = Action {
    if (gameStore.contains(name)) {
      val game = gameStore.getGame(name).get

      game match {
        case g: BasicTicTacToe =>
          Ok(views.html.SimpleGameBoard(g))
      }

    } else {
      Redirect(routes.HomeController.index())
    }
  }

}
