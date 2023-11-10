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

    fun flipCards(card1 : Card,card2: Card) {
        card.isRevealed = !card.isRevealed
    }
}
