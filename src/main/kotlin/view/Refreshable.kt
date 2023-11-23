package view

import entity.Card

interface Refreshable {
    /**
     * abstract method for refresh after pass
     */
    fun refreshAfterPass():Unit{}

    /**
     * abstract method for refresh after remove pair
     */
    fun refreshAfterRemovePair(isValid:Boolean):Unit{}

    /**
     * abstract method for refresh after reveal card
     */
    fun refreshAfterRevealCard(card:Card):Unit{}

    /**
     * abstract method for refresh after flip card from draw stack
     */
   fun refreshAfterFlip() : Unit{}

    /**
     * abstract method for refresh after changing player
     */
    fun refreshAfterChangePlayer():Unit{}

    /**
     * abstract method for refresh after start game
     */
    fun refreshAfterStartGame():Unit{}

    /**
     * abstract method for refresh after ending game
     */
    fun refreshAfterEndGame():Unit{}
}