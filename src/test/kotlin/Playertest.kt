import entity.Player
import kotlin.test.*
class Playertest {
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