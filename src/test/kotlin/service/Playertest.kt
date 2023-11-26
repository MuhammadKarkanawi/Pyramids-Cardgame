package service
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import entity.Player
import org.junit.jupiter.api.Assertions.assertNotEquals


class PlayerTest {

    @Test
    fun testPlayerInitialization() {
        val playerName = "Alice"
        val player = Player(playerName)

        assertEquals("Alice", player.name)
        assertEquals(0, player.score)
    }

    @Test
    fun testScoreIncrement() {
        val player = Player("Bob")

        // Increment the score
        player.score += 10
        assertNotEquals("Alice", player.name)
        assertEquals("Bob", player.name)
        assertEquals(10, player.score)
    }
}
