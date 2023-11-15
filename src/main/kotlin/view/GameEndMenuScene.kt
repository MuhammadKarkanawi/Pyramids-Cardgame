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

    private fun Player.scoreString() : String = "${this.name} scored ${this.score} points."
    private fun gameResultString(): String {
        val p1Score = rootService.gameService.player1.score
        val p2Score = rootService.gameService.player2.score
        return when {
            p1Score - p2Score > 0 -> "${rootService.gameService.player1.name} wins the game."
            p1Score - p2Score < 0 -> "${rootService.gameService.player2.name} wins the game."
            else -> "Draw. No winner."
        }
    }

     fun refreshAfterGameEnd() {
        val game = rootService.gameService
        checkNotNull(game) { "No game running" }

        p1Score.text = game.player1.scoreString()
        p2Score.text = game.player2.scoreString()
        gameResult.text = gameResultString()

    }

}
