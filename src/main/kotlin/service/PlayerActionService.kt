package service

import AbstractRefreshingService
import entity.*

class PlayerActionService(val rootService: RootService) : AbstractRefreshingService() {
    fun pass() {    //get current game from root service
        // get current game from root service
        val game = rootService.gameService
        checkNotNull(game)
        //get opponentPassed Boolean
        //get current pyramid from root service
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //end game if previous Player already chose Pass
        if (pyramide.opponentPassed) {
            game.endGame()
        }
        //otherwise switch to the other player and set Boolean
        else {
            pyramide.opponentPassed = true
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
        val isValid: Boolean = game.checkCardChoice(card1, card2)

        //get current player
      //  val player: Player = pyramide.currentPlayer

        //remove cards
        if (isValid) {
            //award points
            if (card1.value.toString() == "A" || card2.value.toString() == "A") {
               pyramide.currentPlayer.score++
            } else {
                pyramide.currentPlayer.score+2
            }

            removeCards(card1, card2)

            //update opponentPassed Boolean
            onAllRefreshables { refreshAfterRemovePair(isValid) }
        }

        val isEmpty = game.isEmpty()
        //check if Pyramide.Pyramid entity has cards left and end game if not
        if (isEmpty) {
            game.endGame()
            onAllRefreshables { refreshAfterEndGame() }
        } else {
            //Reveal any new cards that need to be revealed
            game.flipPyramidCards()
            onAllRefreshables { refreshAfterRemovePair( isValid ) }
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

        val pyramideCards: MutableList<MutableList<Card?>> = pyramide.cards

        //check validity of choosen cards and save result
        var isValid: Boolean = game.checkCardChoice(card1, card2)
        var reserveStacksize: Int = pyramide.reserveStack.size
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

    fun revealCard() : Card {
        //get current pyramid from root service
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //get current game from root service
        val game= rootService.gameService
        //checkNotNull(game)

        //get DrawStack from root service
        var drawStack = pyramide.drawStack
        //checkNotNull(drawStack)

        //get reserve Stack from root service
        var reserveStack = pyramide.reserveStack

        checkNotNull(reserveStack)

        //check if DrawStack has at least one card
        var drawStackSize: Int = drawStack.size

        fun isValid(): Boolean {
            return drawStackSize != 0
        }
        /**
         * reveal card from draw stack and add it to reserve stack
         */
        var toDrawnCard: Card? = drawStack[0]
        checkNotNull(toDrawnCard)
        if (isValid()) {
            //player reveals the first card from the drawing stack


            toDrawnCard.isRevealed = true

            //player removes the first card from the stack
            drawStack.removeAt(0)

            //add drawn card to reserve stack, all cards in the reserve Stack get pushed to the right
            reserveStack.add(0, toDrawnCard)

            //reset Pass counter
            pyramide.opponentPassed = false

            // refresh after reveal card
            onAllRefreshables { refreshAfterRevealCard(toDrawnCard)

            //change player
            game.changePlayer()

            //refresh after change player
            onAllRefreshables { refreshAfterChangePlayer() }

            }
        }
        return toDrawnCard
    }
}