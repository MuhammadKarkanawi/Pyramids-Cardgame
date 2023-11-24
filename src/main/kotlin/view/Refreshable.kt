package view

import entity.Card

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
     * abstract method for refresh after reveal card
     */
    fun refreshAfterRevealCard(card:Card){}

    /**
     * abstract method for refresh after flip card from draw stack
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