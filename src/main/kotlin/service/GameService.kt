package service

import AbstractRefreshingService
import entity.*
import service.*

class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * two player initialiesed
     */
    var player1: Player = Player("")
    var player2: Player = Player("")

    /**
     * this methode start and initialise a new game
     */
    fun startGame(player1Name: String, player2Name: String) {
        //var pyramide = rootService.

       // checkNotNull(pyramide)
        var pyramide = Pyramide()
        player1 = Player(player1Name)
        player2 = Player(player2Name)

        val playerListe: MutableList<Player> = mutableListOf()

        playerListe.add(0, player1)
        playerListe.add(1, player2)

        pyramide.playerList=playerListe

        pyramide = distributeCards(pyramide)

        rootService.currentGame = pyramide

        rootService.addRefreshables()
        onAllRefreshables { refreshAfterStartGame() }

    }

    fun changePlayer(): Player {
        val pyramide = rootService.currentGame

        checkNotNull(pyramide)

        if (pyramide.indexPlayer == 0) {
            pyramide.indexPlayer = 1
        if(pyramide.indexPlayer == 1){
            pyramide.indexPlayer = 0
        }
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterChangePlayer() }

            return pyramide.currentPlayer
        } else {
            pyramide.indexPlayer = 0

            rootService.addRefreshables()
            onAllRefreshables { refreshAfterChangePlayer() }

            return pyramide.currentPlayer
        }


    }

    fun checkCardChoice(card1: Card, card2: Card): Boolean {


        // Überprüfe, ob die Summe der Werte der beiden Karten 15 ergibt und sie nicht beide Asse sind
        val sum = card1.value.toInt() + card2.value.toInt()

      return   card1.value.toString() != "A" && card2.value.toString() == "A" ||
               card1.value.toString() == "A" && card2.value.toString() != "A" ||
               sum == 15
    }

    /**
     * isEmpty check the pyramid is free from cards
     */
    fun isEmpty(): Boolean {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        val pyramidCards = pyramide.pyramid.cards

        checkNotNull(pyramidCards)

        for (row in 0 until 7) {
            for (col in 0 until row + 1) {

                if (pyramidCards[row][col] != null) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * when the game ends will show the winner
     */
    fun endGame() {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        println("GAME OVER")
        println("The Winner Is :")
        if (pyramide.playerList[0].score > pyramide.playerList[1].score) {
            println(pyramide.playerList[0].name)
        } else if (pyramide.playerList[0].score < pyramide.playerList[1].score) {
            println(pyramide.playerList[1].name)
        } else {
            println(pyramide.playerList[0].name + pyramide.playerList[1].name)
        }

        showResult()
        rootService.addRefreshables()
        onAllRefreshables { refreshAfterEndGame() }

    }

    /**
     * will show the results
     */
    fun showResult() {
        println("Player1 :" + player1.score + "Player2:" + player2.score)
    }

    /**
     * Distributes a set of cards for a new game.
     */
    fun distributeCards(pyramide: Pyramide): Pyramide {
        // Get a shuffled list of 52 cards
        val cards = defaultRandomCardList()
        // val pyramide = rootService.currentGame
        // checkNotNull(pyramide)

        createPyramid(cards.take(28), pyramide)
        pyramide.drawStack.cards.addAll(cards.drop(28))
        return pyramide

    }

    /**
     * Generates a shuffled list of 52 cards for a new game.
     *
     * @return A shuffled list of 52 cards.
     */
    private fun defaultRandomCardList(): List<Card> = List(52) { index ->
        Card(
            CardSuit.values()[index % 4],
            CardValue.values()[index % 13]
        )
    }.shuffled()

    /**
     * to creat and initialise new pyramid with cards from card list
     */
    private fun createPyramid(cards: List<Card>, pyramide: Pyramide): Pyramide {
            var i = 0
          var pyramidCards = mutableListOf<MutableList<Card>>()

        for (row in 0 until 7) {
            pyramidCards.add(mutableListOf())
            for (col in 0 until row + 1) {
                pyramidCards[row].add(cards[i])
                i++
            }
        }
         pyramide.pyramid.cards = pyramidCards
        return pyramide
    }

    /**
     * Reveals new outer cards in the pyramid after a card has been removed.
     */
    fun flipPyramidCards() {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val pyramid = currentGame.pyramid
        for(i in 0 until pyramid.cards.size){
            pyramid.cards[i].first().isRevealed =true
            pyramid.cards[i].last().isRevealed = true
        }
    }
}
