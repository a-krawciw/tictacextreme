package services

import model.GameStore
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import javax.inject.Inject

case class CreateData  @Inject ()(gameName: String, playerName: String)
                                 (implicit gameStore: GameStore){
  val isValid: Option[CreateData] = {
      if (gameStore.contains(gameName)) {
        println("Verified false")
        None
      }
      Some(this)
  }

}

object CreateDataConstraints {
  val createDataConstraint: Constraint[CreateData] = Constraint[CreateData](Some("text.constraint"), Nil)(gameData =>
    gameData.isValid.map(msg => Invalid(ValidationError(msg.gameName + " already in use. Please choose another"))).getOrElse(Valid)
  )

}