package entity

import entity.Card
import entity.CardSuit
import entity.CardValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestCard {
    /**
     * Testet, dass die Erstellung eines Card mit gültige werte für Methode isRevealed */
    @Test
    fun caseOne() {

// Testdaten erzeugen

        val CardSuit :CardSuit =CardSuit.SPADES
        val CardValue : CardValue=CardValue.TWO
        val isRevealed : Boolean = false
        // Zu testende Methode mit Testdaten aufrufen
        var createdCard :Card= Card (suit = CardSuit, value = CardValue)

// Test, ob die Werte im neuen Card mit den übergebenen wert übereinstimmt
        assertEquals (isRevealed, createdCard.isRevealed)

        /**
         * Testet, dass die Erstellung eines Card mit ungültige initialisierung für isRevealed fehlschlägt */
        @Test
        fun  caseTwo(){
            // Testdaten erzeugen
            var CardSuit :CardSuit = entity.CardSuit.SPADES
            var CardValue : CardValue= entity.CardValue.TWO
            var createdCard :Card= Card (suit = CardSuit, value = CardValue)
            var isRevealed : Boolean= true
            // Test: create Card schlägtfehl
            assertFailsWith<IllegalArgumentException> {
                // Zu testende Methode mit Testdaten aufrufen
                createdCard

            }
        }
    }
}

