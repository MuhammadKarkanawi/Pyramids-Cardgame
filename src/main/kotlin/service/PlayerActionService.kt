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
        rootService.addRefreshables()
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
            if (card1.value == CardValue.ACE || card2.value == CardValue.ACE) {
               pyramide.currentPlayer.score++
            } else {
                pyramide.currentPlayer.score+2
            }

            removeCards(card1, card2)

            //update opponentPassed Boolean
            rootService.addRefreshables()
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
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterChangePlayer() }
        }
    }

    private fun removeCards(card1: Card, card2: Card) {

        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        val game = rootService.gameService
        checkNotNull(game)

        val pyramideCards = pyramide.pyramid
        if(!pyramide.reserveStack.cards.isEmpty()) {
            //check validity of choosen cards and save result
            if (card1 == pyramide.reserveStack.cards[0]) {
                pyramide.reserveStack.cards.remove(card1)
            }
            if (card2 == pyramide.reserveStack.cards[0]) {
                pyramide.reserveStack.cards.remove(card2)
            }
        }
        for(i in pyramideCards.cards.indices.reversed()){
            pyramideCards.cards[i].remove(card1)
            pyramideCards.cards[i].remove(card2)
            if(pyramideCards.cards[i].isEmpty()){
                pyramideCards.cards.removeAt(i)
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
        var drawStackSize: Int = drawStack.cards.size

        fun isValid(): Boolean {
            return drawStackSize != 0
        }
        /**
         * reveal card from draw stack and add it to reserve stack
         */
        var toDrawnCard: Card? = drawStack.cards[0]
        checkNotNull(toDrawnCard)
        if (isValid()) {
            //player reveals the first card from the drawing stack


            toDrawnCard.isRevealed = true

            //player removes the first card from the stack
            drawStack.cards.removeAt(0)

            //add drawn card to reserve stack, all cards in the reserve Stack get pushed to the right
            reserveStack.cards.add(0, toDrawnCard)

            //reset Pass counter
            pyramide.opponentPassed = false

            // refresh after reveal card
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterRevealCard(toDrawnCard)

            //change player
            game.changePlayer()

            //refresh after change player
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterChangePlayer() }

            }
        }
        return toDrawnCard
    }
}