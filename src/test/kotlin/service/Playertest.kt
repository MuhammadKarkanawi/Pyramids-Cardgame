package service
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import entity.Player

/**class Playertest {
    @Test
    fun creatPlayerCaseOne () {
// Testdaten erzeugen
        val playerName = "john"
        var playerScore =0

        // Zu testende Methode mit Testdaten aufrufen
        var createdPlayer: Player = Player(playerName)
// Test, ob die Werte im neuen Modul mit den übergebenen wert übereinstimmt
        assertEquals (playerName , createdPlayer.name)
        assertEquals (playerScore, createdPlayer.score )

        /**
         * Testet, dass die Erstellung eines Moduls mit leerem Namen fehlschl gt */
        @Test
        fun creatPlayerCaseTwo () {
// Testdaten erzeugen
            val playerName = ""
            var playerScore = 0
// Test: create Module schlägtfehl
            assertFailsWith<IllegalArgumentException> {
                // Zu testende Methode mit Testdaten aufrufen
                var createdPlayer: Player = Player(playerName)
            }
        }
    }
}
 */




class PlayerTest {

    @Test
    fun testPlayerInitialization() {
        val playerName = "Alice"
        val player = Player(playerName)

        assertEquals(playerName, player.name)
        assertEquals(0, player.score)
    }

    @Test
    fun testScoreIncrement() {
        val player = Player("Bob")

        // Increment the score
        player.score += 10

        assertEquals(10, player.score)
    }
}
