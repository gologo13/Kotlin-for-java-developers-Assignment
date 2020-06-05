package rationals

import java.math.BigInteger

data class Rational(val numerator: BigInteger, val denominator: BigInteger) : Comparable<Rational> {

    override fun compareTo(other: Rational): Int {
        val (numerator1, denominator1) = this.toNorm()
        val (numerator2, denominator2) = other.toNorm()

        return (numerator1 * denominator2).compareTo(numerator2 * denominator1)
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Rational) {
            return false
        }

        val thisNorm = this.toNorm()
        val otherNorm = other.toNorm()

        return thisNorm.numerator == otherNorm.numerator && thisNorm.denominator == otherNorm.denominator
    }

    override fun toString(): String {
        val (numerator1, denominator1) = this.toNorm()
        return if (denominator1 == BigInteger.valueOf(1L)) {
            "$numerator1"
        } else {
            "$numerator1/$denominator1"
        }
    }

    private fun toNorm(): Rational {
        val gcd = this.denominator.gcd(this.numerator)

        var newNum = this.numerator.divide(gcd)
        var newDenom = this.denominator.divide(gcd)

        if (newDenom.signum() == -1) {
            newNum = newNum.negate()
            newDenom = newDenom.negate()
        }

        return Rational(newNum, newDenom)
    }

    override fun hashCode(): Int {
        var result = numerator.hashCode()
        result = 31 * result + denominator.hashCode()
        return result
    }
}

operator fun Rational.plus(other: Rational): Rational {
    // 1/2 + 1/3 = 1 * 3 + 2 * 2/ 2 * 3 = 5/6
    return Rational(
        this.numerator * other.denominator + other.numerator * this.denominator,
        this.denominator * other.denominator
    )
}

operator fun Rational.minus(other: Rational): Rational {
    return Rational(
        this.numerator * other.denominator - other.numerator * this.denominator,
        this.denominator * other.denominator
    )
}

operator fun Rational.times(other: Rational): Rational {
    // 1/2 * 2/3 = 1 *　2 / 2 * 3
    return Rational(
        this.numerator * other.numerator,
        this.denominator * other.denominator
    )
}

operator fun Rational.div(other: Rational): Rational {
    // 1/2 / 1/3 = 1/2 * 3/1 = 3/2
    return this.times(other.reverse())
}

private fun Rational.reverse(): Rational = Rational(this.denominator, this.numerator)

operator fun Rational.unaryMinus(): Rational = Rational(this.numerator.negate(), this.denominator)

infix fun Int.divBy(other: Int): Rational = Rational(this.toBigInteger(), other.toBigInteger())
infix fun Long.divBy(other: Long): Rational = Rational(this.toBigInteger(), other.toBigInteger())
infix fun BigInteger.divBy(other: BigInteger): Rational = Rational(this, other)

fun String.toRational(): Rational {
    val ret = this.split("/")
    return if (ret.size == 1) Rational(this.toBigInteger(), BigInteger.ONE)
    else Rational(ret[0].toBigInteger(), ret[1].toBigInteger())
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}