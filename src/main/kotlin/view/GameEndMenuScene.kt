package view

import service.RootService
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import entity.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * [MenuScene] that is displayed when the game is finished. It shows the final result of the game
 * as well as the score. Also, there are two buttons: one for starting a new game and one for
 * quitting the program.
 */
class GameEndMenuScene(private val rootService: RootService) : MenuScene(400, 1080) , Refreshable {

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 22)
    )

    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 125)

    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 160)

    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 195).apply {
    }

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 265, text = "Quit").apply {
        visual = ColorVisual(Color(221,136,136))
    }

    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 265, text = "New Game").apply {
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        opacity = .5
        addComponents(headlineLabel, p1Score, p2Score, gameResult, newGameButton, quitButton)
    }

    /**
     * Generates a string representing the player's name and score.
     *
     * @return A formatted string: "<player name> scored <score> points."
     */
    private fun Player.scoreString() : String = "${this.name} scored ${this.score} points."

    /**
     * Generates a string representing the result of the game.
     *
     * @return A string indicating the winner or a draw.
     */
    private fun gameResultString(): String {
        val p1Score = rootService.currentGame!!.playerList[0].score
        val p2Score = rootService.currentGame!!.playerList[1].score
        return when {
            p1Score - p2Score > 0 -> "${rootService.currentGame!!.playerList[0].name} wins the game."
            p1Score - p2Score < 0 -> "${rootService.currentGame!!.playerList[1].name} wins the game."
            else -> "Draw. No winner."
        }
    }
    /**
     * Refreshes the UI after the end of the game, updating player scores and displaying the game result.
     *
     * Throws an exception if no game is running.
     */
     override fun refreshAfterEndGame() {
        val pyramide= rootService.currentGame
        checkNotNull(pyramide) { "No game running" }

        p1Score.text = pyramide.playerList[0].scoreString()
        p2Score.text = pyramide.playerList[1].scoreString()
        gameResult.text = gameResultString()
    }
}
