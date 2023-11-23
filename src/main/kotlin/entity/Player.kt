package entity


/**
 *  Erstellt einen Spieler mit dem angegebenen Namen und einer anfänglichen Punktzahl von 0.
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
}