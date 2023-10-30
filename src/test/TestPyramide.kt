
import kotlin.test.*
class TestPyramide {
    /**
     * Testet, dass die Erstellung eines Pyramide mit gültige werte */
    @Test
    fun CaseOne () {
// Testdaten erzeugen
        var opponentPassed : Boolean=false
        var currentPlayer : Int=1
        var reserveStack  : CardStack = CardStack()
        var drawStack : CardStack = CardStack()
        val player1 :Player = Player( "John", 0)
        val player2 :Player = Player("Noah",0)
        val gamePyramid : Pyramid = Pyramid()
// Test, ob die Werte im neuen Pyramide mit den übergebenen wert übereinstimmt
        var createdPyramide: Pyramide()

        assertEquals (opponentPassed,createdPyramide.opponentPassed)
        assertEquals (currentPlayer,createdPyramide.currentPlayer)
        assertEquals (reserveStack,createdPyramide.reserveStack)
        assertEquals (drawStack,createdPyramide.drawStack)
        assertEquals (player1,createdPyramide.player1)
        assertEquals (player2,createdPyramide.player2)
        /**
         * Testet, dass die Erstellung eines Pyramide mit ungültige werte ^fehlschlägt */
        @Test
        fun CaseTwo (){
            // Testdaten erzeugen
            var opponentPassed : Boolean=true
            var currentPlayer : Int=1
            var reserveStack  : CardStack = CardStack()
            var drawStack : CardStack = CardStack()
            val player1 :Player = Player( "John", 0)
            val player2 :Player = Player("Noah",0)
            val gamePyramid : Pyramid = Pyramid()
// Test: createdModule mit falscher opponentPassed initialisierung schlägtfehl
            var createdPyramide: Pyramide()

            assertFailsWith<IllegalArgumentException> {
                // Zu testende Methode mit Testdaten aufrufen
                val createdPyramid: Pyramid
            }
            @Test
            fun CaseThree (){
            // Testdaten erzeugen
            var opponentPassed : Boolean=false
            var currentPlayer : Int !=1
            var reserveStack  : CardStack = CardStack()
            var drawStack : CardStack = CardStack()
            val player1 :Player = Player( "John", 0)
            val player2 :Player = Player("Noah",0)
            val gamePyramid : Pyramid = Pyramid()
// Test: createdModule mit falscher currentPlayer initialisierung schlägtfehl
            var createdPyramide: Pyramide()

            assertFailsWith<IllegalArgumentException> {
                // Zu testende Methode mit Testdaten aufrufen
                val createdPyramid: Pyramid
            }
        }
            @Test
            fun CaseFour (){
                // Testdaten erzeugen
                var opponentPassed : Boolean=false
                var currentPlayer : Int =1
                var reserveStack  : CardStack = CardStack()
                var drawStack : CardStack = CardStack()
                val player1 :Player = Player( "", 0)
                val player2 :Player = Player("Noah",0)
                val gamePyramid : Pyramid = Pyramid()
// Test: createdModule mit falscher leeren Player1.name  schlägtfehl
                var createdPyramide: Pyramide()

                assertFailsWith<IllegalArgumentException> {
                    // Zu testende Methode mit Testdaten aufrufen
                    val createdPyramid: Pyramid
                }
            }

            @Test
            fun CaseFive (){
                // Testdaten erzeugen
                var opponentPassed : Boolean=false
                var currentPlayer : Int =1
                var reserveStack  : CardStack = CardStack()
                var drawStack : CardStack = CardStack()
                val player1 :Player = Player( "John", 0)
                val player2 :Player = Player("",0)
                val gamePyramid : Pyramid = Pyramid()
// Test: createdModule mit falscher leeren Player2.name  schlägtfehl
                var createdPyramide: Pyramide()

                assertFailsWith<IllegalArgumentException> {
                    // Zu testende Methode mit Testdaten aufrufen
                    val createdPyramid: Pyramid
                }
            }
    }
}
}