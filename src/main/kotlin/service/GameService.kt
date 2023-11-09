package service

import entity.*

class GameService(private val rootService: RootService) {
    val player1: Player = Player("playerOne")
    val player2: Player = Player("playerTwo")
    var currentPlayer: Player = player1
    var reserveStack: ArrayDeque<Card> = ArrayDeque(24)
    fun startGame() {
        val player1 = Player("player1Name")
        val player2 = Player("player2Name")
    }

    fun changePlayer() {
        currentPlayer = if (currentPlayer == player1) player2 else player1
    }

    fun checkCardChoice(card1: Card, card2: Card): Boolean {
        // Überprüfe, ob die Summe der Werte der beiden Karten 15 ergibt und sie nicht beide Asse sind

        var sum = card1.value.toString() + card2.value.toString()
        return sum.toInt() == 15 && !(card1.value.toString() == "ACE" && card2.value.toString() == "ACE")
    }

    fun isEmpty(): Boolean {
        val currentGame: Pyramide? = rootService.currentGame
        checkNotNull(currentGame)
        val emptyPyramid = currentGame.cards.all { row -> row.all { it == null } }
        return emptyPyramid
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
//erstellen & mischen vom decks
    fun distributeCards(card: Card) {
        var cardListe: ArrayDeque<Card> = ArrayDeque(52)
        var newPyramid: Array<Array<Card?>> = Array(7) { row -> Array(row + 1) { card } }
        newPyramid.shuffle()
        var newStack: CardStack

    }

    fun createDrawStack(): CardStack {
        return CardStack()
    }

    fun createPyramid(): Pyramid {
        return Pyramid()
    }

    fun flipCards(card: Card) {
        card.isRevealed = !card.isRevealed
    }
}
