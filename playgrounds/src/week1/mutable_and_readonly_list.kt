package intro.week1

fun main() {
    val mutableList = mutableListOf("Java")
    mutableList.add("Kotlin")
    println(mutableList)

    val readonlyList = listOf("Java")
    // readonlyList.add("Kotlin")
}