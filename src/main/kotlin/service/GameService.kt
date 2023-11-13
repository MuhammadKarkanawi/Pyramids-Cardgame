package service

import AbstractRefreshingService
import entity.*

class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * two player initialiesed
     */
    val player1: Player = Player("playerOne")
    val player2: Player = Player("playerTwo")

    /**
     * this methode start and initialise a new game
     */
    fun startGame() {
        val pyramide = rootService.currentGame

        checkNotNull(pyramide)

        var playerListe: MutableList<Player> = pyramide.playerList

        playerListe.add(0, player1)
        playerListe.add(1, player2)

        distributeCards()

        onAllRefreshables { refreshAfterStartGame() }
    }

    fun changePlayer(): Player {
        var pyramide = rootService.currentGame

        checkNotNull(pyramide)

        if (pyramide.indexPlayer == 0) {
            pyramide.indexPlayer = 1
            pyramide.currentPlayer = pyramide.playerList[pyramide.indexPlayer]
            return pyramide.currentPlayer
        } else {
            pyramide.indexPlayer = 0
            pyramide.currentPlayer = pyramide.playerList[pyramide.indexPlayer]
            return pyramide.currentPlayer
        }

        onAllRefreshables { refreshAfterChangePlayer() }
    }

    fun checkCardChoice(card1: Card, card2: Card): Boolean {


        // Überprüfe, ob die Summe der Werte der beiden Karten 15 ergibt und sie nicht beide Asse sind
        var sum = card1.value.toInt() + card2.value.toInt()
        return sum == 15 && !(card1.value.toString() == "A" && card2.value.toString() == "A")
    }

    /**
     * isEmpty check the pyramid is free from cards
     */
    fun isEmpty(): Boolean {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        var pyramidCards = pyramide.cards

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

        println("GAME OVER")
        println("The Winner Is :")
        if (player1.score > player2.score) {
            println(player1.getName())
        } else if (player1.score < player2.score) {
            println(player2.getName())
        } else {
            println(player1.getName() + player2.getName())
        }
        showResult()
        onAllRefreshables { refreshAfterEndGame() }

    }

    /**
     * will show the results
     */
    private fun showResult() {
        println("Player1 :" + player1.getScore() + "Player2:" + player2.getScore())
    }

    /**
     * Distributes a set of cards for a new game.
     */
    fun distributeCards() {
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

    /**
     * to creat and initialise new pyramid with cards from card list
     */
    private fun createPyramid(cards: List<Card>) {
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)
        var pyramidCards = pyramide.cards
        var cardIndex = 0 // To keep track of which card from the list to use
        for (row in 0 until 7) {
            for (col in 0 until row + 1) {
                if (cards.isNotEmpty()) {
                    pyramidCards[row][col] = cards[cardIndex]
                    cardIndex++
                    if (col == 0 || col == row) {
                        pyramidCards[row][col]?.isRevealed = true // Reveal the outer cards
                    }

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
                card.isRevealed = true
            }
            if (lastUnrevealed != -1 && lastUnrevealed != firstUnrevealed) {
                var card = pyramide.cards[row][lastUnrevealed]
                checkNotNull(card)
                card.isRevealed = true
            }
        }
    }
}
