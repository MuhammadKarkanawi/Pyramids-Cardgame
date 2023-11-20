package entity

/**
 * the board pyramid consisting of 7 rows of cards
 */
class Pyramid() {

    var cards: MutableList<MutableList<Card?>> = mutableListOf()

    fun isEmpty(): Boolean {
        // Check if all elements in the pyramid are null.
        return cards.all { row -> row.all { it == null } }

        /**
         * returns the top card from the stack *without removing* it from the stack.
         * Use [draw] if you want the card also to be removed.
         */

    }
}