package service

import entity.*
import kotlin.test.*

/**
 * Testklasse für die Klasse [PlayerActionService].
 */
class PlayerActionServiceTest  {

    private fun setUpGame() {
        val rootService = RootService()
        val game= rootService.gameService

        game.startGame("John" , "Nik")
        val pyramide = rootService.currentGame
        checkNotNull(pyramide)
        pyramide.cards[1][0] = Card(CardSuit.CLUBS, CardValue.SEVEN)
        pyramide.cards[1][0]?.isRevealed  = true
        pyramide.cards[3][2] = Card(CardSuit.CLUBS, CardValue.EIGHT)
        pyramide.cards[3][2]?.isRevealed = true
        pyramide.cards[4][0] = Card(CardSuit.HEARTS, CardValue.ACE)
        pyramide.cards[4][0]?.isRevealed = true
        pyramide.cards[5][0] = Card(CardSuit.HEARTS, CardValue.TWO)
        pyramide.cards[5][0]?.isRevealed = true

        pyramide.reserveStack.add(0, Card(CardSuit.CLUBS, CardValue.ACE))
        pyramide.reserveStack[0]?.isRevealed = true

    }


    /**
     * Testet mithilfe von [setUpGame] folgende Situationen von [testRemovePair]
     *
     * - 2 Asse werden ausgewählt
     * - 2 Karten welche nicht zusammen 15 ergeben werden ausgewählt
     * - 2 Karten aus der Pyramide, welche ein gültiges Kartenpaar bilden, werden ausgewählt
     * - Ein gültiges Kartenpaar, wovon eine Karte ein Ass aus dem Reservestapel ist, wird ausgewählt
     */
    @Test
    fun testRemovePair() {

        setUpGame()
        val mc = RootService()
        val pyramide = mc.currentGame
        checkNotNull(pyramide)
        val game = mc.gameService

        val sevenCard = pyramide.cards[1].get(0)
        checkNotNull(sevenCard)
        val eigthCard = pyramide.cards[3].get(2)
        checkNotNull(eigthCard)
        val aceCard = pyramide.cards[4].get(0)
        checkNotNull(aceCard)
        val twoCard = pyramide.cards[5].get(0)
        checkNotNull(twoCard)
        val reserveAceCard = pyramide.reserveStack.get(0)
        checkNotNull(reserveAceCard)

        val rootService = RootService()
        assertFailsWith <IllegalStateException>() {rootService.playerActionService.removePair(sevenCard, eigthCard)}

        assertFails("Cards arent Valid") { rootService.playerActionService.removePair(aceCard, reserveAceCard) }
        assertFails("Cards arent Valid") { rootService.playerActionService.removePair(sevenCard, twoCard) }

        rootService.playerActionService.removePair(sevenCard, eigthCard)
        assertEquals(2, game.player1.score)
        assertNull(pyramide.cards[1].get(0))
        assertNull(pyramide.cards[3].get(2))
        assertTrue(pyramide.cards[3].get(1)?.isRevealed!!)



        rootService.playerActionService.removePair(twoCard, reserveAceCard)
        assertEquals(1, game.player2.score)
        assertNull(pyramide.cards[5].get(0))
        assertTrue(pyramide.reserveStack.isEmpty())
        assertTrue(pyramide.cards[5].get(1)?.isRevealed!!)


        pyramide.reserveStack.add(0,sevenCard)
        sevenCard.isRevealed = false

    }
}
