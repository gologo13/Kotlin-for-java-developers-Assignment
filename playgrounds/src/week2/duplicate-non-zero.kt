package week2

fun duplicateNonZero(list: List<Int>): List<Int> {
    return list.flatMap {
        if (it == 0) return listOf()
        listOf(it, it)
    }
}

fun duplicateNonZeroLabel(list: List<Int>): List<Int> {
    return list.flatMap {
        if (it == 0) return@flatMap listOf<Int>()
        listOf(it, it)
    }
}

fun duplicateNonZeroLabel2(list: List<Int>): List<Int> {
    return list.flatMap l@{
        if (it == 0) return@l listOf<Int>()
        listOf(it, it)
    }
}

fun duplicateNonZeroLambda(list: List<Int>): List<Int> {
    return list.flatMap(fun (e): List<Int> {
        if (e == 0) return listOf<Int>()
        return listOf(e, e)
    })
}

fun main() {
    println(duplicateNonZero(listOf(3, 0, 5)))
    println(duplicateNonZeroLabel(listOf(3, 0, 5)))
    println(duplicateNonZeroLabel2(listOf(3, 0, 5)))
    println(duplicateNonZeroLambda(listOf(3, 0, 5)))
}
