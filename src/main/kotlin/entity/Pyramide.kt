package entity
import entity.*
/**
 * represents a game of Pyramide.
 */

class Pyramide {

    var pyramidCards : Array<Array<Card?>> = Array(7) { row -> Array(row + 1) { null }}

    var playerList: MutableList<Player> = mutableListOf()
    /**
     * a stack of cards indicating the drawStack
     */
    var toDrawStack : MutableList<Card?> = CardStack().drawStack
    /**
     * a stack of cards indicating the reserveStack
     */
    var toReserveStack : MutableList<Card?>  =CardStack().reserveStack
    /**
     * flag indicating whether an opponent passed the previous turn
     */
    var opponentPassed : Boolean=false
    /**
     * Int value indicating the current player (1= Player1; 2=Player2)
     */
    var currentPlayer : Int=0

    /**
     * initializing both players
     */
    val player1 :Player = Player( "John")
    val player2 :Player = Player("Nick")

    /**
     * represents the pyramid of cards on the board
     */
    val gamePyramid : Pyramid = Pyramid()

    //init{toDrawStack.}
}