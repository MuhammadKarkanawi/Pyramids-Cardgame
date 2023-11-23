package entity
import kotlin.random.Random

/**
 * Erstellt einen CardStack mit der angegebenen Liste von Karten.
 *
 * @param cards Die Liste von Karten, die den Stapel initialisieren.
 */
class CardStack (var cards : MutableList<Card> ) {

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [draw] if you want the card also to be removed.
     */

    fun peek() : Card  = cards.first()


}