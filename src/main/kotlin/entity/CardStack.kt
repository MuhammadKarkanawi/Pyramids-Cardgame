package entity

/**
 * Erstellt einen CardStack mit der angegebenen Liste von Karten.
 *
 * @param cards Die Liste von Karten, die den Stapel initialisieren.
 */
class CardStack (var cards : MutableList<Card> ) {

    /**
     * returns the top card from the stack *without removing* it from the stack.
     */
    fun peek() : Card  = cards.first()


}