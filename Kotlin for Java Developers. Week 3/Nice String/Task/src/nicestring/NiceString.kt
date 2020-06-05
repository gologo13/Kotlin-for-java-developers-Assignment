package nicestring

fun String.isNice(): Boolean {
    return listOf(
        meetsFirstCondition(this),
        meetsSecondCondition(this),
        meetsThirdCondition(this)
    ).count { it } >= 2
}

fun meetsFirstCondition(s: String): Boolean {
    return setOf("bu", "ba", "be").none { s.contains(it) }
}

fun meetsSecondCondition(s: String): Boolean {
    val vowels = listOf('a', 'e', 'i', 'o', 'u')
    return s.toCharArray().count { vowels.contains(it) } >= 3
}

fun meetsThirdCondition(s: String): Boolean {
//    return s.windowed(2).any {it[0] == it[1] }

    return s.zipWithNext().any { it.first == it.second }

//    return ('a' .. 'z').map { "${it}${it}" }.any { s.contains(it) }
}
