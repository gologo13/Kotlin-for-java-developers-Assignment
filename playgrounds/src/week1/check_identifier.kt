package intro.week1

fun isValidIdentifier(s: String): Boolean {
    fun isValidCharacter(ch: Char) = ch == '_' || ch.isLetterOrDigit()

    if (s.isEmpty() || s[0].isDigit()) return false

    for (ch in s) {
        if (!isValidCharacter(ch)) return false
    }

    return true
}

fun my_isValidIdentifier(s: String): Boolean {
    if (s.isEmpty()) {
        return false
    }

    if (s[0] !in 'a' .. 'z' && s[0] != '_') {
        return false
    }

    for (ch in s.slice(1 until s.length)) {
        if (ch !in 'a' .. 'z' && ch !in '1' .. '9') {
            return false
        }
    }

    return true
}

fun main(args: Array<String>) {
    println(isValidIdentifier("name"))   // true
    println(isValidIdentifier("_name"))  // true
    println(isValidIdentifier("_12"))    // true
    println(isValidIdentifier(""))       // false
    println(isValidIdentifier("012"))    // false
    println(isValidIdentifier("no$"))    // false
}