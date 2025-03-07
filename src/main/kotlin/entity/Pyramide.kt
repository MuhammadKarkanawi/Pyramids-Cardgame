package entity

/**
 * represents a game Pyramide.
 */
class Pyramide {
    /**
     * flag indicating whether an opponent passed the previous turn
     */
    var opponentPassed: Boolean = false

    /**player list*/
    var playerList: MutableList<Player> = mutableListOf()

    /** player Index feststellen*/

    var indexPlayer: Int = 0

    /**
     * Int value indicating the current player (0= Player1; 1=Player2)
     */
    val currentPlayer: Player
        get() = playerList[indexPlayer]

    /**
     * a stack of cards indicating the drawStack
     */
    var drawStack: CardStack = CardStack(mutableListOf())

    /**
     * a stack of cards indicating the reserveStack
     */
    var reserveStack: CardStack = CardStack(mutableListOf())

    /**cards from pyramid */
    var pyramid : Pyramid = Pyramid()
    var cards: MutableList<Card> = mutableListOf()

}