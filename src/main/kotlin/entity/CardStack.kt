package entity

class CardStack () {

    var cards : MutableList<MutableList<Card?>> = Pyramid().cards

    val drawStack : MutableList<Card?> = mutableListOf()
    val reserveStack : MutableList<Card?> = mutableListOf()


    fun getDrawStack():MutableList<Card?> {return this.drawStack}
    fun getReserveStack(): MutableList<Card?>{return this.reserveStack}


}