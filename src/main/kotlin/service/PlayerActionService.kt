package service
import entity.*

/**
 * A class responsible for managing player actions and interactions during the game
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Passes the turn to the next player or ends the game if both players have passed.
     */
    fun pass() {
        // get current game from root service
        val game = rootService.gameService

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

    /**
     * Removes a valid pair of cards from the pyramid, awards points, and manages game progress.
     * @param card1 The first card to be removed.
     * @param card2 The second card to be removed.
     */
    fun removePair(card1: Card, card2: Card) {
        //get current game from rootService
        val game = rootService.gameService

        //get current pyramide from rootService
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //check validity of choosen cards and save result
        val isValid: Boolean = game.checkCardChoice(card1, card2)

        //remove cards
        if (isValid) {
            //award points
            if ((card1.value == CardValue.ACE).xor(card2.value == CardValue.ACE)) {
               pyramide.currentPlayer.score++
            } else {
                pyramide.currentPlayer.score += 2
            }
            removeCards(card1, card2)

            //update opponentPassed Boolean
            pyramide.opponentPassed=false

            rootService.addRefreshables()
        onAllRefreshables { refreshAfterRemovePair(isValid) }
        }
        val isEmpty = rootService.currentGame!!.pyramid.isEmpty()
        //check if Pyramide.Pyramid entity has cards left and end game if not
        if (isEmpty) {
            game.endGame()
            onAllRefreshables { refreshAfterEndGame() }
        } else {
            //Reveal any new cards that need to be revealed
            game.flipPyramidCards()
            onAllRefreshables { refreshAfterFlip() }
            //change to next player
            game.changePlayer()
            //refresh view
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterChangePlayer() }
        }
    }

    /**
     * Removes chosen cards from the pyramid and reserve stack.
     * @param card1 The first card to be removed.
     * @param card2 The second card to be removed.
     */
    private fun removeCards(card1: Card, card2: Card) {

        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        val pyramideCards = pyramide.pyramid
        if(pyramide.reserveStack.cards.isNotEmpty()) {
            //check validity of choosen cards and save result
            if (card1 == pyramide.reserveStack.cards[0]) {
                pyramide.reserveStack.cards.remove(card1)
            }
            else if (card2 == pyramide.reserveStack.cards[0]) {
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
    /**
     * Reveals a card from the draw stack and adds it to the reserve stack.
     *
     * @return The revealed card.
     */
    fun revealCard() : Card {
        //get current pyramid from root service
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)

        //get current game from root service
        val game= rootService.gameService
        //checkNotNull(game)

        //get DrawStack from root service
        val drawStack = pyramide.drawStack
        //checkNotNull(drawStack)

        //get reserve Stack from root service
        val reserveStack = pyramide.reserveStack

        //check if DrawStack has at least one card
        val drawStackSize: Int = drawStack.cards.size

        fun isValid(): Boolean {
            return drawStackSize != 0
        }
        /**
         * reveal card from draw stack and add it to reserve stack
         */
        val toDrawnCard: Card = drawStack.cards[0]

        if (isValid()) {
            //player reveals the first card from the drawing stack
            toDrawnCard.isRevealed = true

            //player removes the first card from the stack
            drawStack.cards.removeAt(0)

            //add drawn card to reserve stack, all cards in the reserve Stack get pushed to the right
            reserveStack.cards.add(0, toDrawnCard)

            //reset Pass counter
            pyramide.opponentPassed = false

            //change player
            game.changePlayer()
            // refresh after reveal card
            rootService.addRefreshables()
            onAllRefreshables { refreshAfterRevealCard(toDrawnCard)
            onAllRefreshables { refreshAfterChangePlayer() }

            }
        }
        return toDrawnCard
    }
}