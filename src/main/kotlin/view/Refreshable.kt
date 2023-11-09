package view

interface Refreshable {
    fun refreshAfterPass():Unit{}
    fun refreshAfterRemovePair(isValid:Boolean):Unit{}
    fun refreshAfterRevealCard():Unit{}
    fun refreshAfterChangePlayer():Unit{}
    fun refreshAfterStartGame():Unit{}
    fun refreshAfterEndGame():Unit{}
}