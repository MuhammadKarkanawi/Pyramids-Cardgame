package service

import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class TestPyramid {

    @Test
    fun testIsEmptyWhenEmpty() {
        val pyramid = Pyramid()

        assertTrue(pyramid.isEmpty())
    }

    @Test
    fun testIsEmptyWhenNotEmpty() {
        val pyramid = Pyramid()

        // Add a non-empty row to the pyramid
        pyramid.cards.add(mutableListOf(Card(CardSuit.HEARTS, CardValue.ACE)))

        assertFalse(pyramid.isEmpty())
    }
}
