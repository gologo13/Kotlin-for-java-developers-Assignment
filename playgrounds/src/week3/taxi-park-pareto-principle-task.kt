package week3

fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false

    val totalIncome = trips.sumByDouble { it.cost }
    val sortedDriversIncome: List<Double> = trips
        .groupBy { it.driver }
        .map { (_, tripsByDriver) -> tripsByDriver.sumByDouble { it.cost } }
        .sortedDescending()

    val numberOfTopDrivers = (0.2 * allDrivers.size).toInt()
    val incomeByTopDrivers = sortedDriversIncome
        .take(numberOfTopDrivers)
        .sum()

    return incomeByTopDrivers >= 0.8 * totalIncome
}

fun main(args: Array<String>) {
    taxiPark(1..5, 1..4,
        trip(1, 1, 20, 20.0),
        trip(1, 2, 20, 20.0),
        trip(1, 3, 20, 20.0),
        trip(1, 4, 20, 20.0),
        trip(2, 1, 20, 19.0))
        .checkParetoPrinciple() eq true

    taxiPark(1..5, 1..4,
        trip(1, 1, 20, 20.0),
        trip(1, 2, 20, 20.0),
        trip(1, 3, 20, 20.0),
        trip(1, 4, 20, 20.0),
        trip(2, 1, 20, 21.0))
        .checkParetoPrinciple() eq false
}


/////////

data class TaxiPark(
    val allDrivers: Set<Driver>,
    val allPassengers: Set<Passenger>,
    val trips: List<Trip>)

data class Driver(val name: String)
data class Passenger(val name: String)

data class Trip(
    val driver: Driver,
    val passengers: Set<Passenger>,
    // the trip duration in minutes
    val duration: Int,
    // the trip distance in km
    val distance: Double,
    // the percentage of discount (in 0.0..1.0 if not null)
    val discount: Double? = null
) {
    // the total cost of the trip
    val cost: Double
        get() = (1 - (discount ?: 0.0)) * (duration + distance)
}

fun driver(i: Int) = Driver("D-$i")
fun passenger(i: Int) = Passenger("P-$i")
fun drivers(range: IntRange) = range.toList().map(::driver).toSet()

fun passengers(indices: List<Int>) = indices.map(::passenger).toSet()
fun passengers(range: IntRange) = passengers(range.toList())
fun passengers(vararg indices: Int) = passengers(indices.toList())

fun taxiPark(driverIndexes: IntRange, passengerIndexes: IntRange, vararg trips: Trip) =
    TaxiPark(drivers(driverIndexes), passengers(passengerIndexes), trips.toList())

fun trip(driverIndex: Int, passenger: Int, duration: Int = 10, distance: Double = 3.0, discount: Double? = null) =
    Trip(driver(driverIndex), passengers(passenger), duration, distance, discount)

infix fun <T> T.eq(other: T) {
    if (this == other) println("OK")
    else println("Error: $this != $other")
}