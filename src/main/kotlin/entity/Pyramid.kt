package entity

/**
 * the board pyramid consisting of 7 rows of cards
 */
class Pyramid {

    var cards: MutableList<MutableList<Card>> = mutableListOf()

    /**
     *Check if all elements in the pyramid are null.
     */
    fun isEmpty(): Boolean {

        return cards.isEmpty()}
}