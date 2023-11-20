package entity
import kotlin.random.Random
class CardStack () {

    var cards : MutableList<MutableList<Card?>> = Pyramid().cards

    val drawStack : MutableList<Card?> = mutableListOf()
    val reserveStack : MutableList<Card?> = mutableListOf()

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [draw] if you want the card also to be removed.
     */

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [draw] if you want the card also to be removed.
     */

    /**
     * returns the top card from the stack *without removing* it from the stack.
     * Use [draw] if you want the card also to be removed.
     */
    fun peek() : Card? = reserveStack.first()

    /**
     * provides a view of the full stack contents without changing it. Use [draw]
     * for actually drawing cards from this stack.
     */
    fun peekAll(): List<Card?> = reserveStack.toList()

}