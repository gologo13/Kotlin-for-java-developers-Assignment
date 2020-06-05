package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
        Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val pair = initializer.nextValue(this)
    if (pair != null) {
        this[pair.first] = pair.second
    }
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
    if (rowOrColumn.all { cell -> this[cell] != null } || rowOrColumn.all { cell -> this[cell] == null }) {
        return false
    }

    val mergedNumberList = rowOrColumn
        .map { cell -> this[cell] }
        .moveAndMergeEqual { it * 2 }
        .toMutableList()

    rowOrColumn.forEach { cell ->
        if (mergedNumberList.isNotEmpty()) {
            this[cell] = mergedNumberList[0]
            mergedNumberList.removeAt(0)
        } else {
            this[cell] = null
        }
    }

    return true
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    val nestedCells = mutableListOf<List<Cell>>()
    when (direction) {
        Direction.UP    -> for (i in 1..4) {
            nestedCells.add(getColumn(1..4, i))
        }
        Direction.DOWN  -> for (i in 4 downTo 1) {
            nestedCells.add(getColumn(4 downTo 1, i))
        }
        Direction.RIGHT -> for (i in 1..4) {
            nestedCells.add(getRow(i, 4 downTo 1))
        }
        Direction.LEFT  -> for (i in 4 downTo 1) {
            nestedCells.add(getRow(i, 1..4))
        }
    }

    /*
        return nestedCells.map { cells ->
        val b = this.moveValuesInRowOrColumn(cells)
        println("cells: ${cells}")
        println("moreValuesInRowOrColumn: ${b}")
        return b
    }.any { it }
     */

    return nestedCells.map { this.moveValuesInRowOrColumn(it) }.any { it }
}