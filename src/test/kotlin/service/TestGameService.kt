package service
import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import view.Refreshable
class TestGameService {


    private fun setUpGame(vararg refreshables: Refreshable): RootService {
        val rootService = RootService()
        val gameService = rootService.gameService
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        refreshables.forEach { rootService.addRefreshable(it) }

        val pyramidCards = listOf(


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
            Card(CardSuit.SPADES, CardValue.KING)
        )


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
            Card(CardSuit.HEARTS, CardValue.SIX)
        )


        var i = 0
        val pyramid = mutableListOf<MutableList<Card>>()

        for (row in 0 until 7) {
            pyramid.add(mutableListOf())
            for (col in 0 until row + 1) {
                pyramid[row].add(pyramidCards[i])
                i++
            }
        }

        gameService.startGame("Bob", "Alice")

        currentGame.pyramid.cards = pyramid
        currentGame.drawStack.cards.addAll(drawStackCards)


        println(currentGame)
        return rootService
    }

    @Test
    fun testStartGame() {
        val refreshableTest = RefreshableTest()
        val mc =RootService()
        assertNull(mc.currentGame)

        assertFalse(refreshableTest.refreshAfterStartGameCalled)
        refreshableTest.reset()

        val rootService =setUpGame(refreshableTest)
        val gameService = rootService.gameService
        assertTrue(refreshableTest.refreshAfterStartGameCalled)

        assertEquals("Player1", gameService.player1.name)
        assertEquals("Player2", gameService.player2.name)

        // Verify that the game pyramid is created and initialized
        assertNotNull(rootService.currentGame)
        assertNotNull(rootService.currentGame?.pyramid)
        assertEquals(24, rootService.currentGame?.drawStack?.cards?.size)
        assertEquals(7, rootService.currentGame?.pyramid?.cards?.size)
        assertEquals(3, rootService.currentGame?.pyramid?.cards!![2].size)
        gameService.endGame()
        refreshableTest.reset()

        assertFalse(refreshableTest.refreshAfterStartGameCalled)
        assertTrue(refreshableTest.refreshAfterEndGameCalled)
    }

    @Test
    fun testChangePlayer() {
        val refreshableTest = RefreshableTest()
        val rootService =setUpGame(refreshableTest)
        val gameService = rootService.gameService
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)

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
        val refreshableTest = RefreshableTest()
        val rootService =setUpGame(refreshableTest)
        val gameService = rootService.gameService
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)

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
        val refreshableTest = RefreshableTest()
        val rootService =setUpGame(refreshableTest)
        val gameService = rootService.gameService
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)

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
        val refreshableTest = RefreshableTest()
        val rootService =setUpGame(refreshableTest)
        val gameService = rootService.gameService
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)


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
        val rootService =setUpGame(refreshableTest)
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)

        assertFalse(refreshableTest.refreshAfterPassCalled)
        refreshableTest.reset()

        rootService.playerActionService.pass()
        assertTrue(refreshableTest.refreshAfterStartGameCalled)

        rootService.playerActionService.pass()
        assertTrue(refreshableTest.refreshAfterEndGameCalled)

    }

    @Test
    fun testRevealCard() {
        val refreshableTest = RefreshableTest()
        val rootService =setUpGame(refreshableTest)
        val currentGame = rootService.currentGame
        assertNotNull(currentGame)

        assertFalse(refreshableTest.refreshAfterRevealCardCalled)
        refreshableTest.reset()

        rootService.playerActionService.revealCard()

        assertNotEquals(Card(CardSuit.HEARTS, CardValue.SIX),currentGame!!.drawStack.cards[0])
        assertEquals(Card(CardSuit.HEARTS, CardValue.SIX), currentGame.reserveStack.cards[0] )

        assertTrue(refreshableTest.refreshAfterRevealCardCalled)
    }
}