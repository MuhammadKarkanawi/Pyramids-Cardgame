package service

import AbstractRefreshingService
import entity.*

class GameService(private val rootService: RootService) :AbstractRefreshingService() {


    val player1: Player = Player("playerOne")
    val player2: Player = Player("playerTwo")


    fun startGame() {
        var pyramide = rootService.currentGame

        checkNotNull(pyramide)

        var playerListe :MutableList<Player> = pyramide.playerList

        playerListe.add(0,player1)
        playerListe.add(1,player2)

        var reserveStack: MutableList<Card?> = pyramide.reserveStack

        var drawStack : MutableList<Card?> = pyramide.drawStack

        var createPyramid :Pyramid = pyramide.newPyramid

        onAllRefreshables { refreshAfterStartGame() }
    }

    fun changePlayer() {
        var pyramide = rootService.currentGame

        checkNotNull(pyramide)

        var currentPlayer : Player =pyramide.currentPlayer
        currentPlayer = if (currentPlayer == player1) player2 else player1

        onAllRefreshables { refreshAfterChangePlayer() }
    }

    fun checkCardChoice(card1: Card, card2: Card): Boolean {


        // Überprüfe, ob die Summe der Werte der beiden Karten 15 ergibt und sie nicht beide Asse sind
        var sum = card1.value.toInt() + card2.value.toInt()
        return sum == 15 && !(card1.value.toString() == "A" && card2.value.toString() == "A" )
    }

    fun isEmpty(pyramid: Pyramid): Boolean {
        val pyramide : Pyramide? = rootService.currentGame
        checkNotNull(pyramide)

        val ifEmptyPyramid = pyramide.newPyramid.cards
            for(i in 0..6)
                { if(!ifEmptyPyramid[i].isEmpty())
                    {return false}
                }
        return true
    }

    fun endGame() {
        println("GAME OVER")
        println("The Winner Is :")
        if (player1.score > player2.score) {
            println("player1.getName()")
        } else if (player1.score < player2.score) {
            println("player2.getName()")
        } else {
            println("$(player1.getName()) , $(player2.getName())")
        }
        showResult()
    }

    private fun showResult() {
        println("Player1 : $(player1.getScore()) , Player2: $( player2.getScore())")
    }
    /**
     * Distributes a set of cards for a new game.
     */
    fun distributeCards(card: Card) {
    val cards = defaultRandomCardList() // Get a shuffled list of 52 cards
    val pyramide = rootService.currentGame
    checkNotNull(pyramide)

    createPyramid(cards.take(28))
    pyramide.drawStack.addAll(cards.drop(28))

    }

    /**
     * Generates a shuffled list of 52 cards for a new game.
     *
     * @return A shuffled list of 52 cards.
     */
    private fun defaultRandomCardList(): List<Card> = List(52) { index ->
        Card(
            CardSuit.values()[index % 4],
            CardValue.values()[index % 13 + 2]
        )
    }.shuffled()

    fun createDrawStack(): CardStack {
        return CardStack()
    }

    private fun createPyramid(cards: List<Card>) {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        var cardIndex = 0 // To keep track of which card from the list to use
        for (row in 0 until 7) {
            for (col in 0 until row + 1) {
                if (cards.isNotEmpty()) {
                    val card = cards[cardIndex++]
                    if (col == 0 || col == row || row == 6) {
                        card.isRevealed= true // Reveal the outer and bottom cards
                    }
                    pyramide.cards[row][col] = card
                }
            }
        }
    }

    /**
     * Reveals new outer cards in the pyramid after a card has been removed.
     */
    fun flipCard() {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)
        val rows = pyramide.cards.size

        for (row in 0 until rows) {
            val firstUnrevealed = pyramide.cards[row].indexOfFirst { it != null && !it.isRevealed }
            val lastUnrevealed = pyramide.cards[row].indexOfLast { it != null && !it.isRevealed }

            if (firstUnrevealed != -1) {
               var card = pyramide.cards[row][firstUnrevealed]
                checkNotNull(card)
                card.isRevealed=true
            }
            if (lastUnrevealed != -1 && lastUnrevealed != firstUnrevealed) {
                var card = pyramide.cards[row][lastUnrevealed]
                checkNotNull(card)
                card.isRevealed = true
            }
        }
    }
}
