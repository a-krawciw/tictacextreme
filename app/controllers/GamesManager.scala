package controllers

import model.{BasicTicTacToe, ExtremeTicTacToe, GameStore}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.Constraints
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Cookie, Request, RequestHeader}
import services.CreateData

import javax.inject.Inject

class GamesManager @Inject() (cc: ControllerComponents)
                             (implicit gameStore: GameStore, messagesApi: MessagesApi) extends AbstractController(cc) {



  val gameForm: Form[CreateData] = Form(
    mapping(
      "Game Name" -> text.verifying(Constraints.nonEmpty(errorMessage="A game name is required")),
      "Player Name" -> text
    )(CreateData.apply)(CreateData.unapply))


  def createGame(): Action[AnyContent] = Action { implicit request =>

    val filledForm = gameForm.bindFromRequest

    filledForm.fold(
      badForm => BadRequest(views.html.createGame(badForm)(messagesApi.preferred(request), request)),
      formData => {
        gameStore.addGame(formData.gameName, new ExtremeTicTacToe(3))
        Redirect(routes.GamesManager.showGame(formData.gameName)).withCookies(Cookie("playerName", formData.playerName))
      })
  }

  def joinGame(name: String = "") = Action { implicit  request =>
    val pCookie = request.cookies.get("playerName")
    val playerName = if (pCookie.isDefined) pCookie.get.value else ""
    val preFilledForm = gameForm.fill(CreateData(name, playerName))
    Ok(views.html.createGame(preFilledForm)(messagesApi.preferred(request), request))
  }

  def showGame(name: String) = Action { implicit request =>
    if (gameStore.contains(name)) {
      if(request.cookies.get("playerName").isDefined)
        Ok(views.html.simpleGame(gameStore.getGame(name).get))
      else
        Redirect(routes.GamesManager.joinGame(name))
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
        case g: ExtremeTicTacToe =>
          Ok(views.html.ComplexGameBoard(g))
      }

    } else {
      Redirect(routes.HomeController.index())
    }
  }

}
