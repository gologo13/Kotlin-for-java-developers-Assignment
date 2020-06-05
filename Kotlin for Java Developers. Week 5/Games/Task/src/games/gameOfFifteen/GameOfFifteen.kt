package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game
import games.gameOfFifteen.GameOfFifteen.Companion.width

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(width)
    private val lastIndex = width * width - 1

    override fun initialize() {
        val initSeq = initializer.initialPermutation

        for (i in 1..width) {
            for (j in 1..width) {
                val index = (i - 1) * width + (j - 1)
                if (index != lastIndex) {
                    board[Cell(i, j)] = initSeq[index]
                }
            }
        }
    }

    override fun canMove(): Boolean {
        val emptyCell = board.find { it == null }!!

        return Direction.values().any { emptyCell.getNeighbour(it) != null }
    }

    override fun hasWon(): Boolean {
        val seq = board.getAllCells().map { board[it] }

        val ans: MutableList<Int?> = (1..15).toMutableList()
        ans.add(null)

        return seq == ans
    }

    override fun processMove(direction: Direction) {
        val emptyCell = board.find { it == null }!!
        println("emptyCell + Direction: $emptyCell + $direction")

        emptyCell.getNeighbour(direction)?.let { nextCell ->
            println("nextCell: $nextCell")

            // swap
            board[emptyCell] = board.get(nextCell)
            board[nextCell] = null
        }
    }

    override fun get(i: Int, j: Int): Int? {
        return board[Cell(i, j)]
    }

    private fun Cell.getNeighbour(direction: Direction): Cell? {
        val (deltaI, deltaJ) = when (direction) {
            Direction.UP    -> 1 to 0
            Direction.DOWN  -> -1 to 0
            Direction.RIGHT -> 0 to -1
            Direction.LEFT  -> 0 to 1
        }

        val (i, j) = this.i + deltaI to this.j + deltaJ

        return if (i in 1..width && j in 1..width) board.getCell(i, j) else null
    }

    companion object {
        private const val width = 4
    }
}
