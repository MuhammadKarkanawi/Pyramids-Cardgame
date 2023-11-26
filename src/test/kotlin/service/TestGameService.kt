package service
import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import view.Refreshable
class GameServiceTest {


   private fun setUpGame(vararg refreshables: Refreshable) : RootService{
        val rootService = RootService()
        val gameService= rootService.gameService
        val currentGame= rootService.currentGame
        checkNotNull(currentGame)
        refreshables.forEach { rootService.addRefreshable(it) }

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


        val drawStackCards = listOf(
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


        var i = 0
        val pyramid = mutableListOf<MutableList<Card>>()

        for (row in 0 until 7) {
            pyramid.add(mutableListOf())
            for (col in 0 until row + 1) {
                pyramid[row].add(pyramidCards[i])
                i++
            }
        }
        currentGame.pyramid.cards=pyramid
        currentGame.drawStack.cards.addAll(drawStackCards)

        gameService.startGame("Bob", "Alice")
        println(currentGame)
        return rootService
    }
        @Test
        fun testStartGame() {
            val refreshableTest = RefreshableTest()
            val rootService =setUpGame(refreshableTest)
            val gameService= rootService.gameService
            val currentGame = rootService.currentGame
            assertNotNull(currentGame)


            assertFalse(refreshableTest.refreshAfterStartGameCalled)

            refreshableTest.reset()

            gameService.startGame("Player1", "Player2")
            assertTrue(refreshableTest.refreshAfterStartGameCalled)

            assertEquals("Player1", gameService.player1.name)
            assertEquals("Player2", gameService.player2.name)

            // Verify that the game pyramid is created and initialized
            assertNotNull(rootService.currentGame)
            assertNotNull(rootService.currentGame?.pyramid)
            assertEquals(24, rootService.currentGame?.drawStack?.cards?.size)
            assertEquals(7, rootService.currentGame?.pyramid?.cards?.size)
            assertEquals(3, rootService.currentGame?.pyramid?.cards!![3].size)
            gameService.endGame()
            refreshableTest.reset()
            assertFalse(refreshableTest.refreshAfterStartGameCalled)

        }

        @Test
        fun testChangePlayer() {
            val rootService = RootService()
            val gameService= rootService.gameService
            val currentGame = rootService.currentGame
            assertNotNull(currentGame)
            val refreshableTest = RefreshableTest()
            rootService.addRefreshable(refreshableTest)


            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            assertFalse(refreshableTest.refreshAfterChangePlayerCalled)
            refreshableTest.reset()

            // Change the player and verify
            val currentPlayerBeforeChange = rootService.currentGame?.currentPlayer
            val newPlayer = gameService.changePlayer()

            assertNotNull(currentPlayerBeforeChange)
            assertNotNull(newPlayer)
            assertNotEquals(currentPlayerBeforeChange, newPlayer)

            gameService.changePlayer()
            assertTrue(refreshableTest.refreshAfterChangePlayerCalled)

        }

        @Test
        fun testCheckCardChoice() {
            val rootService = RootService()
            val gameService= rootService.gameService
            val currentGame = rootService.currentGame
            assertNotNull(currentGame)

            val refreshableTest = RefreshableTest()
            rootService.addRefreshable(refreshableTest)
            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            assertFalse(refreshableTest.refreshAfterRemovePairCalled)
            refreshableTest.reset()


            // Create two cards for testing
            val card1 = Card(CardSuit.HEARTS, CardValue.ACE)
            val card2 = Card(CardSuit.DIAMONDS, CardValue.TWO)

            currentGame?.pyramid!!.cards[0][0] = card1
            currentGame.pyramid.cards[1][0] = card2
            // Check if the card choice is valid
            val isValid = gameService.checkCardChoice(card1, card2)
            assertTrue(isValid)
            rootService.playerActionService.removePair(card1,card2)

            assertTrue(refreshableTest.refreshAfterRemovePairCalled)
        }

        @Test
        fun testEndGame() {
            val rootService = RootService()
            val gameService= rootService.gameService
            val currentGame = rootService.currentGame
            assertNotNull(currentGame)

            val refreshableTest = RefreshableTest()
            rootService.addRefreshable(refreshableTest)


            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            // Set scores for testing
            currentGame!!.playerList[0].score = 10
            currentGame.playerList[1].score = 15

            assertFalse(refreshableTest.refreshAfterEndGameCalled)
            refreshableTest.reset()
            // End the game and verify the winner
            gameService.endGame()
            assertTrue(refreshableTest.refreshAfterEndGameCalled)

        }

        @Test
        fun testFlipPyramidCards() {
            val rootService = RootService()
            val gameService= rootService.gameService
            val refreshableTest = RefreshableTest()
            val currentGame = rootService.currentGame
            assertNotNull(currentGame)
            rootService.addRefreshable(refreshableTest)


            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            assertFalse(refreshableTest.refreshAfterFlipCalled)
            refreshableTest.reset()


            // Create two cards for testing
            val card1 = Card(CardSuit.HEARTS, CardValue.ACE)
            val card2 = Card(CardSuit.DIAMONDS, CardValue.TWO)

            currentGame?.pyramid!!.cards[0][0] = card1
            currentGame.pyramid.cards[1][0] = card2

            // Check if the card choice is valid
            val isValid = gameService.checkCardChoice(card1, card2)
            assertTrue(isValid)
            rootService.playerActionService.removePair(card1,card2)

            // Flip the pyramid cards and verify
            gameService.flipPyramidCards()

            assertTrue(refreshableTest.refreshAfterFlipCalled)

            assertTrue(currentGame.pyramid.cards.all { it.first().isRevealed && it.last().isRevealed })

        }
    }
