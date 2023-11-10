package entity
import entity.*
/**
 * represents a game of Pyramide.
 */

class Pyramide {

    /**
     * flag indicating whether an opponent passed the previous turn
     */
    var opponentPassed : Boolean=false

    //player list
    var playerList: MutableList<Player> = mutableListOf()

    /**
     * Int value indicating the current player (1= Player1; 2=Player2)
     */
    var currentPlayer : Player = playerList[0]

    /**
     * a stack of cards indicating the drawStack
     */
    var drawStack : MutableList<Card?> = CardStack().drawStack
    /**
     * a stack of cards indicating the reserveStack
     */
    var reserveStack : MutableList<Card?>  =CardStack().reserveStack


    var cards : MutableList<MutableList<Card?>> = mutableListOf()

    var newPyramid: Pyramid = Pyramid()

}