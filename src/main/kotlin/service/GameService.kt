package service

import entity.*

/**
 * Creates a GameService with the provided RootService.
 *
 * @param rootService The RootService to use for game management.
 */
class GameService(private val rootService: RootService) : AbstractRefreshingService() {

    /**
     * two player initialiesed
     */
    var player1: Player = Player("")
    var player2: Player = Player("")

    /**
     * Starts and initializes a new game with two players.
     *
     * @param player1Name The name of the first player.
     * @param player2Name The name of the second player.
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

        pyramide.playerList = playerListe

        pyramide = distributeCards(pyramide)

        rootService.currentGame = pyramide

        rootService.addRefreshables()
        onAllRefreshables { refreshAfterStartGame() }

    }

    /**
     * Changes the active player and triggers a refresh.
     *
     * @return The new active player.
     */
    fun changePlayer(): Player {
        val pyramide = rootService.currentGame

        checkNotNull(pyramide)

        if (pyramide.indexPlayer == 0) {
            pyramide.indexPlayer = 1

            rootService.addRefreshables()
            onAllRefreshables {refreshAfterChangePlayer()}

            return pyramide.currentPlayer
        } else {
            pyramide.indexPlayer = 0

            rootService.addRefreshables()
            onAllRefreshables {refreshAfterChangePlayer()}

            return pyramide.currentPlayer
        }
    }

    /**
     * Checks if the chosen pair of cards forms a valid move.
     *
     * @param card1 The first card.
     * @param card2 The second card.
     * @return True if the move is valid, false otherwise.
     */
    fun checkCardChoice(card1: Card, card2: Card): Boolean {


        // Überprüfe, ob die Summe der Werte der beiden Karten 15 ergibt und sie nicht beide Asse sind
        val sum = card1.value.toInt() + card2.value.toInt()

        return (card1.value == CardValue.ACE).xor(card2.value == CardValue.ACE) ||
        sum == 15
    }

    /**
     * Ends the game and displays the winner.
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
     * Displays the results of the game.
     */
    private fun showResult() {
        println("Player1 :" + player1.score + "Player2:" + player2.score)
    }

    /**
     * Distributes a set of cards for a new game.
     *
     * @param pyramide The pyramid to distribute cards to.
     * @return The updated pyramid.
     */
    private fun distributeCards(pyramide: Pyramide): Pyramide {
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
     * Creates and initializes a new pyramid with cards from the card list.
     *
     * @param cards The list of cards to use for the pyramid.
     * @param pyramide The pyramid to initialize.
     * @return The initialized pyramid.
     */
    private fun createPyramid(cards: List<Card>, pyramide: Pyramide): Pyramide {
        var i = 0
        val pyramidCards = mutableListOf<MutableList<Card>>()

        for (row in 0 until 7) {
            pyramidCards.add(mutableListOf())
            for (j in 0 until row + 1) {
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
        for (i in 0 until pyramid.cards.size) {
            pyramid.cards[i].first().isRevealed = true
            pyramid.cards[i].last().isRevealed = true
        }
    }
}
