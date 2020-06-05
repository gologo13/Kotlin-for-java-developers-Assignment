package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = allDrivers.minus(trips.map { it.driver })

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> = if (minTrips == 0) allPassengers else
    trips
        .flatMap(Trip::passengers)
        .groupingBy { it }
        .eachCount()
        .filterValues { freq -> freq >= minTrips }
        .keys

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> =
    trips
        .filter { it.driver == driver }
        .flatMap(Trip::passengers)
        .groupBy { it }
        .filterValues { group -> group.size > 1 }
        .keys

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =
    allPassengers
        .map {
            passenger -> passenger to trips.filter { passenger in it.passengers }
        }
        .toMap()
        .filterValues { trips -> trips.count { it.discount != null } > trips.size / 2 }
        .keys

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val durationToRange = fun (duration: Int): IntRange {
        val division = duration / 10
        val reminder = duration % 10
        return if (reminder == 0) duration .. duration + 9 else division * 10 .. division * 10 + 9
    }

    return trips
        .map { durationToRange(it.duration) to it }
        .groupBy { it.first }
        .maxBy { it.value.size }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isNullOrEmpty()) {
        return false
    }

    val numOf20percentOfDrivers = (allDrivers.count() * 0.2).toInt()

    val top20percentSuccessfulDrivers = allDrivers
        .map { driver -> driver to trips.filter { it.driver == driver }.sumByDouble(Trip::cost) }
        .sortedByDescending { it.second }
        .take(numOf20percentOfDrivers)

    val top20Income = top20percentSuccessfulDrivers.sumByDouble { it.second }
    val threshold = trips.sumByDouble(Trip::cost) * 0.8

    println(
            "#drivers:\t${allDrivers.count()}\n" +
            "#top20% drivers:\t${numOf20percentOfDrivers}\n" +
            "top20% drivers: ${top20percentSuccessfulDrivers}\n" +
            "top20Income: ${top20Income}\n" +
            "threshold(80% of income): ${threshold}\n" +
            "result(${top20Income} > ${threshold}): ${top20Income > threshold}"
            )

    return top20Income >= threshold
}