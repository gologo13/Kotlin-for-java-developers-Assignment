package week4

var counter = 0
val foo: Int
    get() = counter++

fun main(args: Array<String>) {
    // The values should be different:
    println(foo)
    println(foo)
    println(foo)
}