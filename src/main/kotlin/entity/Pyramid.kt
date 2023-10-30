package entity


/**
 * the board pyramid consisting of 7 rows of cards
 */
class Pyramid() {
    val cards : Array<Array<Card?>> = Array(7) { row -> Array(row + 1) { null } }
}