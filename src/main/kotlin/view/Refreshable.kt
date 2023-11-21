package view

import entity.Card

interface Refreshable {
    /**
     * refresh after pass
     */
    fun refreshAfterPass():Unit{}

    /**
     * refresh after remove pair
     */
    fun refreshAfterRemovePair(isValid:Boolean):Unit{}

    /**
     * refresh after reveal card
     */
    fun refreshAfterRevealCard(card:Card):Unit{}

   fun refreshAfterFlip() : Unit{}

    /**
     * refresh after changing player
     */
    fun refreshAfterChangePlayer():Unit{}

    /**
     * refresh after start game
     */
    fun refreshAfterStartGame():Unit{}

    /**
     * refresh after ending game
     */
    fun refreshAfterEndGame():Unit{}
}