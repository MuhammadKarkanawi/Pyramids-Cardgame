package service

import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Test class for the Pyramid class.
 */
class TestPyramid {

    /**
     * Test case to check if the isEmpty method returns true when the pyramid is empty.
     */
    @Test
    fun testIsEmptyWhenEmpty() {
        val pyramid = Pyramid()

        assertTrue(pyramid.isEmpty())
    }
    /**
     * Test case to check if the isEmpty method returns false when the pyramid is not empty.
     */
    @Test
    fun testIsEmptyWhenNotEmpty() {
        val pyramid = Pyramid()

        // Add a non-empty row to the pyramid
        pyramid.cards.add(mutableListOf(Card(CardSuit.HEARTS, CardValue.ACE)))

        assertFalse(pyramid.isEmpty())
    }
}
