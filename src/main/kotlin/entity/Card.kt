package entity
/**
* Playing card with a specific suit and value
*
* @property suit The suit of the card (e.g. diamonds, hearts..)
* @property value The value of the card (e.g. two, three..)
*/
data class Card(val suit: CardSuit, val value: CardValue) {
  var isRevealed : Boolean= false
  override fun toString() = "$suit$value"
}

