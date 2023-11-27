package view

import entity.Card

/**
 * The `Refreshable` interface defines methods for refreshing the user interface after specific game events.
 */
interface Refreshable {
    /**
     * abstract method for refresh after pass
     */
    fun refreshAfterPass(){}

    /**
     * abstract method for refresh after remove pair
     */
    fun refreshAfterRemovePair(isValid:Boolean){}

    /**
     * abstract method for refresh after reveal card from draw to reserve stack
     */
    fun refreshAfterRevealCard(card:Card){}

    /**
     * abstract method for refresh after flip pyramid cards after removing old ones
     */
   fun refreshAfterFlip(){}

    /**
     * abstract method for refresh after changing player
     */
    fun refreshAfterChangePlayer(){}

    /**
     * abstract method for refresh after start game
     */
    fun refreshAfterStartGame(){}

    /**
     * abstract method for refresh after ending game
     */
    fun refreshAfterEndGame(){}
}