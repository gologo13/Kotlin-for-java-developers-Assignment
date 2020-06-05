package intro.week1

fun main(args: Array<String>) {
    val s = "1"
    println(s as? Int)    // null
    println(s as Int?)    // exception
}