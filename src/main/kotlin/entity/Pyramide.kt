package entity

/**
 * represents a game of Pyramide.
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
    var currentPlayer: Player = playerList[indexPlayer]

    /**
     * a stack of cards indicating the drawStack
     */
    var drawStack: MutableList<Card?> = CardStack().drawStack

    /**
     * a stack of cards indicating the reserveStack
     */
    var reserveStack: MutableList<Card?> = CardStack().reserveStack

    /**cards from pyramid */
    var cards: MutableList<MutableList<Card?>> = Pyramid().cards

    var newPyramid: Pyramid = Pyramid()

}