package entity

data class Card(val suit: CardSuit, val value: CardValue) {
  val isRevealed : Boolean=false
    override fun toString() = "$suit$value"
}

