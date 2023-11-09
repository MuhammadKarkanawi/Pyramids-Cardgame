package entity


/**
 * Constructor for the 2 players
 *
 * @property name The name of the player.
 * @property score The score of the player.
 */
class Player {
    val name : String
    var score : Int
    constructor(name: String)  {
    this.name=name
        this.score=0
       }

    fun getName():String
    {return this.name}
    fun getScore():Int {return this.score}
    fun setScore(i:Int): Unit {this.score + i }
}