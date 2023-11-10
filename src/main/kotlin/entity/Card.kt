package entity
/**
* Playing card with a specific suit and value
*
* @property suit The suit of the card (e.g. diamonds, hearts..)
* @property value The value of the card (e.g. two, three..)
*/
data class Card(val suits: CardSuit, val values: CardValue) {

  val suit : CardSuit = suits

  val value : CardValue = values

  var isRevealed : Boolean= false
  //fun getSuit():CardSuit{return this.typ}

  //fun getValue():CardValue {return this.wert}
  override fun toString():String = "$suit$value"
}

