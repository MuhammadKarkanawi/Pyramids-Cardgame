/**package service

import entity.*
import kotlin.test.*

/**
 * Testklasse für die Klasse [PlayerActionService].
 */
class PlayerActionServiceTest  {

    private fun setUpGame(cards: List<Card>) {
        val rootService = RootService()
        val gameService= rootService.gameService
        val currentGame= rootService.currentGame
        checkNotNull(currentGame)

        gameService.startGame("John" , "Nik")

        var i = 0
        val pyramidCards = mutableListOf<MutableList<Card>>()

        for (row in 0 until 7) {
            pyramidCards.add(mutableListOf())
            for (col in 0 until row + 1) {
                pyramidCards[row].add(cards[i])
                i++
            }
        }
        pyramide.pyramid.cards= pyramidCards

        pyramide.pyramid.cards[1][0] = Card(CardSuit.CLUBS, CardValue.SEVEN)
        pyramide.pyramid.cards[1][0].isRevealed  = true
        pyramide.pyramid.cards[3][2] = Card(CardSuit.CLUBS, CardValue.EIGHT)
        pyramide.pyramid.cards[3][2].isRevealed = true
        pyramide.pyramid.cards[4][0] = Card(CardSuit.HEARTS, CardValue.ACE)
        pyramide.pyramid.cards[4][0].isRevealed = true
        pyramide.pyramid.cards[5][0] = Card(CardSuit.HEARTS, CardValue.TWO)
        pyramide.pyramid.cards[5][0].isRevealed = true

        pyramide.reserveStack= CardStack(mutableListOf())
        pyramide.reserveStack.cards[0]= Card(CardSuit.CLUBS, CardValue.ACE)
        pyramide.reserveStack.cards[0].isRevealed = true

    }


    /**
     * Testet mithilfe von [setUpGame] folgende Situationen von [testRemovePair]
     *
     * - 2 Asse werden ausgewählt
     * - 2 Karten, welche nicht zusammen 15 ergeben werden ausgewählt
     * - 2 Karten aus der Pyramide, welche ein gültiges Kartenpaar bilden, werden ausgewählt
     * - Ein gültiges Kartenpaar, wovon eine Karte ein Ass aus dem Reservestapel ist, wird ausgewählt
     */
    @Test
    fun testRemovePair(cards: List<Card>) {

        setUpGame(cards )
        val mc = RootService()
        val pyramide = mc.currentGame
        checkNotNull(pyramide)

        val sevenCard = pyramide.pyramid.cards[1][0]

        val eigthCard = pyramide.pyramid.cards[3][2]

        val aceCard = pyramide.pyramid.cards[4][0]

        val twoCard = pyramide.pyramid.cards[5][0]

        val reserveAceCard = pyramide.reserveStack.cards[0]


        val rootService = RootService()
        assertFailsWith <IllegalStateException> {rootService.playerActionService.removePair(sevenCard, eigthCard)}

        assertFails("Cards arent Valid") { rootService.playerActionService.removePair(aceCard, reserveAceCard) }
        assertFails("Cards arent Valid") { rootService.playerActionService.removePair(sevenCard, twoCard) }

        rootService.playerActionService.removePair(sevenCard, eigthCard)
        assertEquals(2, pyramide.currentPlayer.score)
        assertNull(pyramide.pyramid.cards[1][0])
        assertNull(pyramide.pyramid.cards[3][2])
        assertTrue(pyramide.pyramid.cards[3][1].isRevealed)



        rootService.playerActionService.removePair(twoCard, reserveAceCard)
        assertEquals(1, pyramide.currentPlayer.score)
        assertNull(pyramide.pyramid.cards[5][0])
        assertTrue(pyramide.reserveStack.cards.isEmpty())
        assertTrue(pyramide.pyramid.cards[5][1].isRevealed)

        sevenCard.isRevealed = false
        pyramide.reserveStack.cards.add(0,sevenCard)
        assertTrue(pyramide.reserveStack.cards[0].isRevealed)
    }
}
*/