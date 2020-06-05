package week4

open class AA(open val value: String) {
    init {
        value.length
    }
}

class B(override val value: String) : AA(value)

fun main(args: Array<String>) {
    B("a")
}