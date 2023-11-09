package entity
import entity.*
class CardStack () {


    val drawStack : MutableList<Card?> = mutableListOf()
    val reserveStack : MutableList<Card?> = mutableListOf()
    var sizeDrawstack: Int = drawStack.size

    fun getDrawStack():MutableList<Card?> {return this.drawStack}
    fun getReserveStack(): MutableList<Card?>{return this.reserveStack}
    fun isEmptyPullstack(): Boolean{return getDrawStack().isEmpty()}

}