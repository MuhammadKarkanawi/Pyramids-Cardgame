package view

import tools.aqua.bgw.core.BoardGameApplication
import service.RootService

    /**
     * Implementation of the BGW [BoardGameApplication] for the example card game "War"
     */
    class PyramidApplication : BoardGameApplication("Pyramid"), Refreshable {

        // Central service from which all others are created/accessed
        // also holds the currently active game
        private val rootService = RootService()

        // Scenes

        // This is where the actual game takes place
        private val gameScene = GameTableScene(rootService)

        // This menu scene is shown after each finished game (i.e. no more cards to draw)
        private val gameEndMenuScene = GameEndMenuScene(rootService).apply {
            newGameButton.onMouseClicked = {
                this@PyramidApplication.showMenuScene(newGameMenuScene)
            }
            quitButton.onMouseClicked = {
                exit()
            }
        }

        /** This menu scene is shown after application start and if the "new game" button
         *is clicked in the gameFinishedMenuScene
        */
        private val newGameMenuScene = NewGameMenuScene(rootService).apply {
            quitButton.onMouseClicked = {
                exit()
            }
        }

        init {

            // all scenes and the application itself need too
            // react to changes done in the service layer
            rootService.addRefreshables(
                this,
                gameScene,
                gameEndMenuScene,
                newGameMenuScene
            )

            // This is just done so that the blurred background when showing
            // the new game menu has content and looks nicer
            //rootService.gameService.startGame("Bob", "Alice")

            this.showGameScene(gameScene)
            this.showMenuScene(newGameMenuScene, 0)

        }

        override fun refreshAfterStartGame() {
            this.hideMenuScene()
        }

        override fun refreshAfterEndGame() {
            this.showMenuScene(gameEndMenuScene)
        }

    }