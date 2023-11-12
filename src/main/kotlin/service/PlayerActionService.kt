package service
import AbstractRefreshingService
import entity.*

class PlayerActionService(val rootService:RootService):AbstractRefreshingService() {
    fun pass() {    //get current game from root service
        val game = rootService.gameService
        checkNotNull(game)
        //get opponentPassed Boolean

        val pyramide = rootService.currentGame
        checkNotNull(pyramide)
        var opponentPassed = pyramide.opponentPassed
        //end game if previous Player already chose Pass
        if (opponentPassed) {
            game.endGame()
        }
        //otherwise switch to the other player and set Boolean
        else {
            opponentPassed = true
            game.changePlayer()
        }
        //refresh view
        onAllRefreshables { refreshAfterPass() }
    }

    fun removePair(card1: Card, card2: Card) {
        //get current game from rootService
        val game = rootService.gameService
        checkNotNull(game)

        //get current pyramide from rootService
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //check validity of choosen cards and save result
        var isValid: Boolean = game.checkCardChoice(card1, card2)

        //get current player
        var player: Player = pyramide.currentPlayer

        //remove cards
        if (isValid) {
            //award points
            if (card1.value.toString() == "A" || card2.value.toString() == "A") {
                player.setScore(1)
            } else {
                player.setScore(2)
            }

            removeCards(card1, card2)

            //update opponentPassed Boolean
            onAllRefreshables { refreshAfterRemovePair(isValid) }
        }
        var isEmpty =game.isEmpty(pyramide.newPyramid)
        //check if Pyramide.Pyramid entity has cards left and end game if not
        if (isEmpty) {
            game.endGame()
            onAllRefreshables { refreshAfterEndGame() }
        } else {
            //Reveal any new cards that need to be revealed
            game.flipCard()
            onAllRefreshables { refreshAfterRevealCard() }
            //change to next player
            game.changePlayer()
            //refresh view
            onAllRefreshables { refreshAfterChangePlayer() }
        }
    }

    private fun removeCards(card1: Card, card2: Card) {

        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        val game = rootService.gameService
        checkNotNull(game)

        var pyramideCards: MutableList<MutableList<Card?>> = pyramide.cards

        //check validity of choosen cards and save result
        var isValid: Boolean = game.checkCardChoice(card1, card2)
        var reserveStacksize : Int = pyramide.reserveStack.size
        if (isValid) {
            if (reserveStacksize != 0) {
                //if erste karte in reserveStack gefunden
                if (card1 in pyramide.reserveStack) {
                    pyramide.reserveStack.removeAt(0)

                    //if zweite karte in Pyramid gefunden
                    for (i in 0..6) {
                        if (card2 == pyramideCards[i][0]) {
                            pyramideCards[i][0] = null
                        } else if (card2 == pyramideCards[i][pyramideCards[i].size - 1]) {
                            pyramideCards[i][pyramideCards[i].size - 1] = null
                        }
                    }
                }
                //if zweite karte in reserveStack gefunden
                else if (card2 in pyramide.reserveStack) {
                    pyramide.reserveStack.removeAt(0)
                    //if erste karte in Pyramid gefunden
                    for (i in 0..6) {
                        if (card1 == pyramideCards[i][0]) {
                            pyramideCards[i][0] = null
                        } else if (card1 == pyramideCards[i][pyramideCards[i].size - 1]) {
                            pyramideCards[i][pyramideCards[i].size - 1] = null
                        }
                    }
                }
            }
            // beide karten im pyramid gefunden
            else {
                for (i in 0..6) {
                    if (card1 == pyramideCards[i][0] || card2 == pyramideCards[i][0]) {
                        pyramideCards[i][0] = null
                    }
                    if (card1 == pyramideCards[i][pyramideCards[i].size - 1] || card2 == pyramideCards[i][pyramideCards[i].size - 1]) {
                        pyramideCards[i][pyramideCards[i].size - 1] = null
                    }
                }
            }
        }
    }

    fun revealCard() {
        //get current game from root service
        val
                pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //get current player from root service
        var currentPlayer = pyramide.currentPlayer

        //get DrawStack from root service
        var drawStack = pyramide.drawStack
        checkNotNull(drawStack)
        //check if DrawStack has at least one card
        var drawStackSize: Int = pyramide.drawStack.size

        fun isValid(): Boolean {
            return drawStackSize != 0
        }

        if (isValid()) {
            //player reveals the first card from the drawing stack
            var drawnCard: Card? = drawStack[0]
            checkNotNull(drawnCard)

            drawnCard.isRevealed = true

            onAllRefreshables {
                refreshAfterRevealCard()

                //player removes the first card from the stack
                drawStack.removeAt(0)
                //add drawn card to reserve stack, all cards in the reserve Stack get pushed to the right
                var reserveStack = pyramide.reserveStack
                reserveStack.add(0, drawnCard)

                //reset Pass counter
                pyramide.opponentPassed = false
                //change player
                rootService.gameService.changePlayer()
                onAllRefreshables { refreshAfterChangePlayer() }
            }
        }
    }
}