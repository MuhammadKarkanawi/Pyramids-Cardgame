package entity

import entity.*
import kotlin.test.*



class TestPyramid {

        @Test
        fun creatPyramidCaseOne () {
// Testdaten erzeugen
            val cards : Array<Array<Card?>> = Array(7) { row -> Array(row + 1) { null } }

            // Zu testende Methode mit Testdaten aufrufen
            val createdPyramid : Pyramid = Pyramid()
// Test, ob die Werte im neuen Pyramid mit den übergebenen wert übereinstimmt
            assertEquals (cards, createdPyramid.cards)
            assertEquals (7, createdPyramid.cards.size)

            /**
             * Testet, dass die Erstellung eines Pyramid mit ungültige Array= null fehlschlägt */
            @Test
            fun createdPyramidCaseTwo (){
            // Testdaten erzeugen
                val cards : Array<Array<Card?>> =Array(0) { row -> Array(row + 1) { null } }


            // Test: create Module schlägtfehl
            var createdPyramid : Pyramid = Pyramid()
            assertFailsWith<IllegalArgumentException> {
                    // Zu testende Methode mit Testdaten aufrufen
                    val createdPyramid: Pyramid
                }
            }
        }
    }
