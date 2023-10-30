package entity

class CardStack () {


    private val cards: ArrayDeque<Card> = ArrayDeque(24)

    val size: Int get() = cards.size


    val empty: Boolean get() = cards.isEmpty()


}