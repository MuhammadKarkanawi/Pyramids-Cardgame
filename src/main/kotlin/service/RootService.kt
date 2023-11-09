package service
import view.Refreshable
import entity.*
import service.*

/**
 * Main class of the service layer for the Pyramid card game. Provides access
 * to all other service classes and holds the [currentGame] state for these
 * services to access.
 */
class RootService(playerOne:Player,playerTwo:Player,pyramid:Pyramid) {

    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)

    /**
     * The currently active game. Can be `null`, if no game has started yet.
     */
    var currentGame : Pyramide? = null

    /**
     * Adds the provided [newRefreshable] to all services connected
     * to this root service
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        AbstractRefreshingService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}
