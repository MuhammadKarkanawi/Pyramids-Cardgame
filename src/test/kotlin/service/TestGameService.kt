package service
import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
class TestGameService {


    private lateinit var rootService: RootService
    private lateinit var playerActionService: PlayerActionService

    @BeforeEach
    fun setUp() {
        // Set up the necessary objects for testing
        rootService = RootService()
        playerActionService = PlayerActionService(rootService)
    }

    @Test
    fun testStartGame() {

        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        assertFalse(refreshableTest.refreshAfterStartGameCalled)
        rootService.gameService.startGame("hi", "ho")


        val gameService = rootService.gameService
        assertTrue(refreshableTest.refreshAfterStartGameCalled)

        assertEquals("hi", gameService.player1.name)
        assertEquals("ho", gameService.player2.name)

        // Verify that the game pyramid is created and initialized
        assertNotNull(rootService.currentGame)
        assertNotNull(rootService.currentGame?.pyramid)
        assertEquals(24, rootService.currentGame?.drawStack?.cards?.size)
        assertEquals(7, rootService.currentGame?.pyramid?.cards?.size)
        assertEquals(3, rootService.currentGame?.pyramid?.cards!![2].size)
        gameService.endGame()
        refreshableTest.reset()

        //assertTrue(refreshableTest.refreshAfterEndGameCalled)
    }

    @Test
    fun testChangePlayer() {
        val gameService = rootService.gameService
        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.startGame("hi", "ho")
        val currentGame = rootService.currentGame!!

        assertFalse(refreshableTest.refreshAfterChangePlayerCalled)
        refreshableTest.reset()

        gameService.changePlayer()
        assertTrue(refreshableTest.refreshAfterChangePlayerCalled)
        refreshableTest.reset()

        gameService.changePlayer()
        assertTrue(refreshableTest.refreshAfterChangePlayerCalled)
        refreshableTest.reset()

    }

    @Test
    fun testCheckCardChoice() {
        val gameService = rootService.gameService
        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.startGame("hi", "ho")
        val currentGame = rootService.currentGame!!

        assertFalse(refreshableTest.refreshAfterRemovePairCalled)
        refreshableTest.reset()

        assertTrue( gameService.checkCardChoice(Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.EIGHT)))

        assertFalse(refreshableTest.refreshAfterRemovePairCalled)
        rootService.playerActionService.removePair(Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.EIGHT))
        assertTrue(refreshableTest.refreshAfterRemovePairCalled)
    }

    @Test
    fun testEndGame() {
        val gameService = rootService.gameService
        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.startGame("hi", "ho")
        val currentGame = rootService.currentGame!!

        // Set scores for testing
        currentGame!!.playerList[0].score = 10
        currentGame.playerList[1].score = 15


        assertEquals(10,currentGame.playerList[0].score)
        assertEquals(15,currentGame.playerList[1].score)

        assertFalse(refreshableTest.refreshAfterEndGameCalled)
        refreshableTest.reset()
        // End the game and verify the winner
        gameService.endGame()
        assertTrue(refreshableTest.refreshAfterEndGameCalled)

    }

    @Test
    fun testFlipPyramidCards() {
        val gameService = rootService.gameService
        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.startGame("hi", "ho")
        val currentGame = rootService.currentGame!!


        assertFalse(refreshableTest.refreshAfterRemovePairCalled)
        refreshableTest.reset()

        assertFalse(refreshableTest.refreshAfterFlipCalled)
        refreshableTest.reset()

        //valide cards from pyramid
        assertTrue( gameService.checkCardChoice(Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.EIGHT)))

        rootService.playerActionService.removePair(Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.EIGHT))

        assertTrue(refreshableTest.refreshAfterRemovePairCalled)
        assertTrue(refreshableTest.refreshAfterFlipCalled)

        //not valide cards from pyramid
        assertFalse( gameService.checkCardChoice(Card(CardSuit.CLUBS,CardValue.NINE),
            Card(CardSuit.DIAMONDS, CardValue.NINE)))
        refreshableTest.reset()

        //valide cards from reserve & pyramide
        assertTrue( gameService.checkCardChoice(Card(CardSuit.SPADES, CardValue.KING),
            Card(CardSuit.SPADES, CardValue.ACE)))

        rootService.playerActionService.removePair(Card(CardSuit.SPADES, CardValue.KING),
            Card(CardSuit.SPADES, CardValue.ACE))

        assertTrue(refreshableTest.refreshAfterRemovePairCalled)
        assertTrue(refreshableTest.refreshAfterFlipCalled)


        assertTrue(currentGame!!.pyramid.cards.all { it.first().isRevealed && it.last().isRevealed })

    }

    @Test
    fun testPass() {
        val refreshableTest = RefreshableTest()
        rootService.addRefreshable(refreshableTest)
        rootService.gameService.startGame("hi", "ho")
        val currentGame = rootService.currentGame!!

        assertFalse(refreshableTest.refreshAfterPassCalled)
        refreshableTest.reset()

        rootService.playerActionService.pass()
        assertTrue(refreshableTest.refreshAfterPassCalled)

        rootService.playerActionService.pass()
        assertTrue(refreshableTest.refreshAfterEndGameCalled)

    }
    /*
        @Test
        fun testRevealCard() {
            val refreshableTest = RefreshableTest()
            rootService.addRefreshable(refreshableTest)
            rootService.gameService.startGame("hi", "ho")
            val currentGame = rootService.currentGame!!

            assertFalse(refreshableTest.refreshAfterRevealCardCalled)
            refreshableTest.reset()

            rootService.playerActionService.revealCard()

            assertNotEquals(Card(CardSuit.HEARTS, CardValue.SIX),currentGame!!.drawStack.cards[0])
            assertEquals(Card(CardSuit.HEARTS, CardValue.SIX), currentGame.reserveStack.cards[0] )

            assertTrue(refreshableTest.refreshAfterRevealCardCalled)
        }
        */

}