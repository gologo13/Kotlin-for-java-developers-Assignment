package board

import board.Direction.DOWN
import board.Direction.LEFT
import board.Direction.RIGHT
import board.Direction.UP

open class SquareBoardImpl<T>(final override val width: Int) : SquareBoard {
    protected val m: MutableList<MutableList<Pair<Cell, T?>>> = mutableListOf()

    init {
        for (i in 0 until width) {
            m.add(mutableListOf())
            for (j in 0 until width) {
                m[i].add(Cell(i + 1, j + 1) to null)
            }
        }
    }

    protected fun getAll(): List<Pair<Cell, T?>> {
        val result = mutableListOf<Pair<Cell, T?>>()
        for (i in 1..width) {
            for (j in 1..width) {
                result.add(get(i, j))
            }
        }
        return result
    }

    protected fun get(i: Int, j: Int): Pair<Cell, T?> {
        return m[i - 1][j - 1]
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return if (i - 1 < width && j - 1 < width) get(i, j).first else null
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException("out of index: i=${i}, j=${j}")
    }

    override fun getAllCells(): Collection<Cell> = getAll().map { it.first }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        if (i > width) return emptyList()

        val result = mutableListOf<Cell>()
        for (j in jRange) {
            if (j <= width) {
                result.add(getCell(i, j))
            }
        }
        return result
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        if (j > width) return emptyList()

        val result = mutableListOf<Cell>()
        for (i in iRange) {
            if (i <= width) {
                result.add(getCell(i, j))
            }
        }
        return result
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val (deltaI, deltaJ) = when (direction) {
            UP    -> -1 to 0
            DOWN  -> 1 to 0
            RIGHT -> 0 to 1
            LEFT  -> 0 to -1
        }

        val (i, j) = this.i + deltaI to this.j + deltaJ

        return if (i in 1..width && j in 1..width) get(i, j).first else null
    }
}

class GameBoardImpl<T>(width: Int) : SquareBoardImpl<T>(width), GameBoard<T> {
    override fun get(cell: Cell): T? {
        val (i, j) = cell
        return get(i, j).let { (_, value) -> value }
    }

    override fun set(cell: Cell, value: T?) {
        val (i, j) = cell
        m[i - 1][j - 1] = cell to value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return getAll().filter { (_, value) -> predicate(value) }.map { it.first }
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).firstOrNull()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate).isNotEmpty()
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return filter(predicate).size == getAll().size
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl<Void>(width)

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

