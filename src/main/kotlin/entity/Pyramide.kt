package entity
/**
 * represents a game of Pyramide.
 */

class Pyramide {
    /**
     * flag indicating whether an opponent passed the previous turn
     */
    var opponentPassed : Boolean=false
    /**
     * Int value indicating the current player (1= Player1; 2=Player2)
     */
    var currentPlayer : Int=1
    /**
     * a stack of cards indicating the reserveStack
     */
    var reserveStack  : CardStack = CardStack()
    /**
     * a stack of cards indicating the drawStack
     */
    var drawStack : CardStack = CardStack()
    /**
     * initializing both players
     */
    val player1 :Player = Player( "John", 0)
    val player2 :Player = Player("Noah",0)

    /**
     * represents the pyramid of cards on the board
     */
    val gamePyramid : Pyramid = Pyramid()
}