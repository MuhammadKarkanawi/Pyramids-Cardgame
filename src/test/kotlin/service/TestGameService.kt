package service
import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GameServiceTest {

        @Test
        fun testStartGame() {
            val rootService = RootService()
            val gameService = GameService(rootService)

            // Test starting a new game
            gameService.startGame("Player1", "Player2")

            // Verify that the players are initialized
            assertEquals("Player1", gameService.player1.name)
            assertEquals("Player2", gameService.player2.name)

            // Verify that the game pyramid is created and initialized
            assertNotNull(rootService.currentGame)
            assertNotNull(rootService.currentGame?.pyramid)
            assertEquals(28, rootService.currentGame?.drawStack?.cards?.size)

            // Verify that the refreshables are called
            // (You need to define appropriate methods to check this based on your framework)
        }

        @Test
        fun testChangePlayer() {
            val rootService = RootService()
            val gameService = GameService(rootService)

            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            // Change the player and verify
            val currentPlayerBeforeChange = rootService.currentGame?.currentPlayer
            val newPlayer = gameService.changePlayer()

            assertNotNull(currentPlayerBeforeChange)
            assertNotNull(newPlayer)
            assertNotEquals(currentPlayerBeforeChange, newPlayer)

            // Verify that the refreshables are called
            // (You need to define appropriate methods to check this based on your framework)
        }

        @Test
        fun testCheckCardChoice() {
            val rootService = RootService()
            val gameService = GameService(rootService)

            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            // Create two cards for testing
            val card1 = Card(CardSuit.HEARTS, CardValue.ACE)
            val card2 = Card(CardSuit.DIAMONDS, CardValue.TWO)

            // Check if the card choice is valid
            val isValid = gameService.checkCardChoice(card1, card2)

            assertTrue(isValid)
        }

        @Test
        fun testEndGame() {
            val rootService = RootService()
            val gameService = GameService(rootService)

            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            // Set scores for testing
            gameService.player1.score = 10
            gameService.player2.score = 15

            // End the game and verify the winner
            gameService.endGame()

            // Verify that the refreshables are called
            // (You need to define appropriate methods to check this based on your framework)
        }

        @Test
        fun testFlipPyramidCards() {
            val rootService = RootService()
            val gameService = GameService(rootService)

            // Start a game to have a valid current game
            gameService.startGame("Player1", "Player2")

            // Flip the pyramid cards and verify
            gameService.flipPyramidCards()

            val currentGame = rootService.currentGame
            assertNotNull(currentGame)
            assertTrue(currentGame?.pyramid?.cards!!.all { it.first().isRevealed && it.last().isRevealed })

            // Verify that the refreshables are called
            // (You need to define appropriate methods to check this based on your framework)
        }
    }
