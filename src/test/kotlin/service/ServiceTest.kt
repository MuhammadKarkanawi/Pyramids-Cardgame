/**package service

import kotlin.test.*
import entity.*
import view.Refreshable
import kotlin.test.*

class ServiceTest {

    /**
     * Class that provides tests for [GameService] and [PlayerActionService] (both at the same time,
     * as their functionality is not easily separable) by basically playing through some sample games.
     * [Refreshable] is used to validate correct refreshing behavior even though no GUI
     * is present.
     */
    class ServiceTest {


        private fun setUpGame(vararg refreshables: Refreshable): RootService {
            val mc = RootService()
            val pyramide = Pyramide() //mc.currentGame
            refreshables.forEach { mc.addRefreshable(it) }
            val game = mc.gameService

            val pyramidCards = listOf(

                // player 1 left bottom of stack
                Card(CardSuit.CLUBS, CardValue.QUEEN),
                Card(CardSuit.SPADES, CardValue.TEN),
                Card(CardSuit.DIAMONDS, CardValue.SEVEN),
                Card(CardSuit.CLUBS, CardValue.EIGHT),
                Card(CardSuit.CLUBS, CardValue.NINE),
                Card(CardSuit.HEARTS, CardValue.KING),
                Card(CardSuit.DIAMONDS, CardValue.QUEEN),
                Card(CardSuit.SPADES, CardValue.QUEEN),

                Card(CardSuit.DIAMONDS, CardValue.JACK),
                Card(CardSuit.SPADES, CardValue.SEVEN),
                Card(CardSuit.DIAMONDS, CardValue.KING),
                Card(CardSuit.DIAMONDS, CardValue.NINE),
                Card(CardSuit.SPADES, CardValue.EIGHT),
                Card(CardSuit.HEARTS, CardValue.TEN),
                Card(CardSuit.HEARTS, CardValue.EIGHT),
                Card(CardSuit.CLUBS, CardValue.JACK),

                Card(CardSuit.HEARTS, CardValue.ACE),
                Card(CardSuit.SPADES, CardValue.NINE),
                Card(CardSuit.CLUBS, CardValue.ACE),
                Card(CardSuit.SPADES, CardValue.JACK),
                Card(CardSuit.HEARTS, CardValue.SEVEN),
                Card(CardSuit.CLUBS, CardValue.SEVEN),
                Card(CardSuit.CLUBS, CardValue.KING),
                Card(CardSuit.DIAMONDS, CardValue.EIGHT),


                Card(CardSuit.CLUBS, CardValue.TEN),
                Card(CardSuit.DIAMONDS, CardValue.TEN),
                Card(CardSuit.HEARTS, CardValue.JACK),
                Card(CardSuit.SPADES, CardValue.KING))

            game.createPyramid(pyramidCards,pyramide)

            val drawStackcards = listOf(
                 Card(CardSuit.DIAMONDS, CardValue.ACE),
                 Card(CardSuit.HEARTS, CardValue.QUEEN),
                 Card(CardSuit.SPADES, CardValue.ACE),
                 Card(CardSuit.HEARTS, CardValue.NINE),

                Card(CardSuit.CLUBS, CardValue.TWO),
                Card(CardSuit.SPADES, CardValue.TWO),
                Card(CardSuit.DIAMONDS, CardValue.TWO),
                Card(CardSuit.CLUBS, CardValue.THREE),
                Card(CardSuit.CLUBS, CardValue.FOUR),
                Card(CardSuit.HEARTS, CardValue.TWO),
                Card(CardSuit.DIAMONDS, CardValue.THREE),
                Card(CardSuit.SPADES, CardValue.THREE),

                Card(CardSuit.DIAMONDS, CardValue.FOUR),
                Card(CardSuit.SPADES, CardValue.FOUR),
                Card(CardSuit.DIAMONDS, CardValue.FIVE),
                Card(CardSuit.DIAMONDS, CardValue.SIX),
                Card(CardSuit.SPADES, CardValue.FIVE),
                Card(CardSuit.HEARTS, CardValue.THREE),
                Card(CardSuit.HEARTS, CardValue.FOUR),
                Card(CardSuit.CLUBS, CardValue.FIVE),

                Card(CardSuit.HEARTS, CardValue.FIVE),
                Card(CardSuit.SPADES, CardValue.SIX),
                Card(CardSuit.CLUBS, CardValue.SIX),
                Card(CardSuit.HEARTS, CardValue.SIX))

            pyramide.drawStack.cards.addAll(drawStackcards)

            mc.gameService.startGame("Bob", "Alice")
            println(mc.currentGame)
            return mc
        }


        /**
         * uses [setUpGame] as source for deterministic card stacks and tests the following situations
         *
         * - drawing from a stack results in the card lying on the play stack
         * - drawing again, even though other player has not drawn, results in an error
         * - evaluation is triggered after both have drawn
         * - if both cards' values are equal, the cards remain on the players' play stacks
         */
        @Test
        fun testDrawAndEvaluate() {
            val testRefreshable = TestRefreshable()
            val mc = setUpGame(testRefreshable)

            assertNotNull(mc.currentGame)
            val p1 = mc.currentGame!!.player1
            val p2 = mc.currentGame!!.player2

            mc.playerActionService.drawFromRightStack(p1)
            assertEquals(WarCard(CardSuit.CLUBS, CardValue.JACK), p1.playStack.peek())

            assertFails { mc.playerActionService.drawFromLeftStack(p1) }
            assertFails { mc.playerActionService.drawFromRightStack(p1) }

            mc.playerActionService.drawFromRightStack(p2)
            assertEquals(WarCard(CardSuit.HEARTS, CardValue.NINE), p1.collectedCardsStack.peek())
            assertTrue(testRefreshable.refreshAfterEvaluateDrawnCardsCalled)
            testRefreshable.reset()

            mc.playerActionService.drawFromLeftStack(p2)
            assertEquals(WarCard(CardSuit.DIAMONDS, CardValue.EIGHT), p2.playStack.peek())

            mc.playerActionService.drawFromRightStack(p1)
            assertEquals(WarCard(CardSuit.HEARTS, CardValue.EIGHT), p1.playStack.peek())

            assertFalse(testRefreshable.refreshAfterEvaluateDrawnCardsCalled)

            mc.playerActionService.drawFromLeftStack(p2)
            assertEquals(WarCard(CardSuit.CLUBS, CardValue.KING), p2.playStack.peek())
            assertEquals(2, p2.playStack.size)

            mc.playerActionService.drawFromLeftStack(p1)
            //  WarCard(CardSuit.SPADES, CardValue.QUEEN),

            assertTrue(testRefreshable.refreshAfterEvaluateDrawnCardsCalled)
            assertEquals(2, p1.collectedCardsStack.size)
            assertEquals(4, p2.collectedCardsStack.size)
        }


        /**
         * Tests the default case of starting a game: instantiate a [RootService] and then run
         * startNewGame on its [RootService.gameService].
         */
        @Test
        fun testStartNewGame() {
            val testRefreshable = TestRefreshable()
            val mc = RootService()
            mc.addRefreshable(testRefreshable)

            assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
            assertNull(mc.currentGame)
            mc.gameService.startNewGame("Bob", "Alice")
            assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
            assertNotNull(mc.currentGame)

            assertEquals(8, mc.currentGame!!.player1.leftDrawStack.size)
            assertEquals(8, mc.currentGame!!.player1.rightDrawStack.size)
            assertEquals(0, mc.currentGame!!.player1.playStack.size)
            assertEquals(0, mc.currentGame!!.player1.collectedCardsStack.size)

            assertEquals(8, mc.currentGame!!.player2.leftDrawStack.size)
            assertEquals(8, mc.currentGame!!.player2.leftDrawStack.size)
            assertEquals(0, mc.currentGame!!.player2.playStack.size)
            assertEquals(0, mc.currentGame!!.player2.collectedCardsStack.size)
        }


        /**
         * Tests if it is possible to to start a game with only two cards in the
         * provided deck (it shouldn't).
         */
        @Test
        fun testFailStartWithTooFewCards() {
            val mc = RootService()
            val testRefreshable = TestRefreshable()
            mc.addRefreshable(testRefreshable)

            // expected to fail, as number of cards must be an even multiple of 4
            assertFails {
                mc.gameService.startNewGame(
                    "Bob", "Alice",
                    listOf(WarCard(CardSuit.DIAMONDS, CardValue.ACE), WarCard(CardSuit.HEARTS, CardValue.SEVEN))
                )
            }
            // as the previous attempt failed, there should be no game started...
            assertNull(mc.currentGame)
            // ... and no refresh triggered
            assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
        }

        /**
         * Test a complete playthrough to test the proper game ending. Uses a a custom (random 32 card)
         * deck built with [CardValue.shortDeck] and checks some arbitrary calls to [Refreshable]
         * methods on the way.
         */
        @Test
        fun testGameEnd() {
            val mc = RootService()
            val testRefreshable = TestRefreshable()
            mc.addRefreshable(testRefreshable)

            val allCards = mutableListOf<WarCard>()
            allCards.shuffle()
            CardSuit.values().forEach { suit ->
                CardValue.shortDeck().forEach() { value ->
                    allCards += WarCard(suit, value)
                }
            }
            mc.gameService.startNewGame("Bob", "Alice", allCards)

            // there should be a game started...
            val currentGame = mc.currentGame
            assertNotNull(currentGame)
            // ... and refresh triggered
            assertTrue(testRefreshable.refreshAfterStartNewGameCalled)

            val p1 = currentGame.player1
            val p2 = currentGame.player2

            // initially, both shouldn't have been triggered
            assertFalse(testRefreshable.refreshAfterDrawFromLeftStackCalled)
            assertFalse(testRefreshable.refreshAfterDrawFromRightStackCalled)

            mc.playerActionService.drawFromLeftStack(p1)
            // now, left should have been triggered
            assertTrue(testRefreshable.refreshAfterDrawFromLeftStackCalled)
            assertFalse(testRefreshable.refreshAfterDrawFromRightStackCalled)

            testRefreshable.reset()

            mc.playerActionService.drawFromRightStack(p2)
            // now, right should have been triggered
            assertFalse(testRefreshable.refreshAfterDrawFromLeftStackCalled)
            assertTrue(testRefreshable.refreshAfterDrawFromRightStackCalled)

            // just cycle through the decks until the end of the game
            // there should be 7 cards left on p1 left and p2 right stack.
            // and 8 cards on p1 right and p2 left stack
            repeat(7) {
                mc.playerActionService.drawFromRightStack(p2)
                mc.playerActionService.drawFromLeftStack(p1)
            }
            repeat(7) {
                mc.playerActionService.drawFromRightStack(p1)
                mc.playerActionService.drawFromLeftStack(p2)
            }

            // game should not have ended yet
            assertFalse(testRefreshable.refreshAfterGameEndCalled)

            // perform the last drawing of cards
            mc.playerActionService.drawFromRightStack(p1)
            mc.playerActionService.drawFromLeftStack(p2)

            // if we reach this point (i.e., no exception),
            // the game should be over and player stacks should be empty
            assertTrue(testRefreshable.refreshAfterGameEndCalled)
            assertEquals(0, p1.leftDrawStack.size)
            assertEquals(0, p1.rightDrawStack.size)
            assertEquals(0, p2.leftDrawStack.size)
            assertEquals(0, p2.rightDrawStack.size)
        }
    }
}
 */