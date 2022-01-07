/**
 * Environment is an object which controls all other class objects.
 *
 * There can only be one environment at a runtime.
 */
object Environment {

    var spots: MutableList<Spot> = mutableListOf()

    var passengers: MutableList<Passenger> = mutableListOf()

    val evtols: MutableList<EVtol> = mutableListOf()

    var distanceMatrix: Array<DoubleArray>? = null

    var destinationMatrix: Array<IntArray>? = null

    /**
     * Takes two [Spot]s as arguments and gets their positions variables.
     * Calls the private [distanceBetweenLocationsInKm] method which calculates the distance
     *
     * @return distance in kilometers
     */
    fun getDistanceBetweenSpotsInKm(fromSpot: Spot, toSpot: Spot): Double? {

        val distanceInKm = fromSpot.position.get(Constants.LATITUDE)
            ?.let {
                toSpot.position.get(Constants.LATITUDE)?.let { it1 ->
                    distanceBetweenLocationsInKm(
                        it, fromSpot.position.get(Constants.LONGITUDE)!!,
                        it1, toSpot.position.get(Constants.LONGITUDE)!!
                    )
                }
            }
        return distanceInKm
    }

    /**
     * Takes the latitude and longitude of two locations as parameter and calculates the distance in kilometers
     *
     * @return distance in kilometers
     */
    fun distanceBetweenLocationsInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var distance =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(
                deg2rad(theta)
            )
        distance = Math.acos(distance)
        distance = rad2deg(distance)
        distance *= Constants.NUMBER_OF_MINUES_IN_A_DEGREE * Constants.STATUTE_MILES_IN_A_NAUTICAL_MILE
        distance *= Constants.ONE_MILE_IN_KM
        return distance
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun getBearing(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Double {
        val latitude1 = Math.toRadians(startLat)
        val latitude2 = Math.toRadians(endLat)
        val longDiff = Math.toRadians(endLng - startLng)
        val y = Math.sin(longDiff) * Math.cos(latitude2)
        val x =
            Math.cos(latitude1) * Math.sin(latitude2) - Math.sin(latitude1) * Math.cos(latitude2) * Math.cos(longDiff)
        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360
    }

    fun getPointByDistanceAndBearing(
        lat: Double,
        lon: Double,
        bearing: Double,
        distanceKm: Double
    ): Pair<Double, Double> {
        val earthRadius = 6378.1

        val bearingR = Math.toRadians(bearing)

        val latR = Math.toRadians(lat)
        val lonR = Math.toRadians(lon)

        val distanceToRadius = distanceKm / earthRadius

        val newLatR = Math.asin(
            Math.sin(latR) * Math.cos(distanceToRadius) +
                    Math.cos(latR) * Math.sin(distanceToRadius) * Math.cos(bearingR)
        )
        val newLonR = lonR + Math.atan2(
            Math.sin(bearingR) * Math.sin(distanceToRadius) * Math.cos(latR),
            Math.cos(distanceToRadius) - Math.sin(latR) * Math.sin(newLatR)
        )

        val latNew = Math.toDegrees(newLatR)
        val lonNew = Math.toDegrees(newLonR)

        return Pair(latNew, lonNew)
    }

    // TODO
    fun adjustWindConditions() {

    }


    fun createDistanceMatrix() {
        this.distanceMatrix = Array(spots.size) { DoubleArray(spots.size) }
        for (row in 0..spots.size - 1) {
            for (column in 0..spots.size - 1) {
                var distanceBetweenDestinations = getDistanceBetweenSpotsInKm(spots.get(row), spots.get(column))!!
                if ((distanceBetweenDestinations <= Constants.MAX_RANGE) && (distanceBetweenDestinations > 0)) {
                    distanceMatrix!![row][column] = distanceBetweenDestinations
                } else {
                    distanceMatrix!![row][column] = -1.00
                }
            }
        }
    }

    fun createDestinationMatrix() {
        this.destinationMatrix = Array(spots.size) { IntArray(spots.size) }

        for (row in 0..spots.size - 1) {
            for (column in 0..spots.size - 1) {
                destinationMatrix!![row][column] = 0
            }
        }
    }

    /**
     * Creates a matrix with all spots
     * The row is the "from" spot of a passenger and the column is the "to" spot of a passenger
     *
     * The indexes in the matrix represent the spot positions
     * The matrix represents following construct:
     *
     *                'San Francisco' 'Cupertino' 'Palo Alto'
     * 'San Francisco'   0              1           3
     * 'Cupertino'       2              0           1
     * 'Palo Alto'       5              2           0
     *
     * The example shows that there are 3 passengers from San Francisco to Palo Alto.
     *
     */
    fun addPassengerToDestinationMatrix(pickUpSpot: Spot, destinationSpot: Spot) {

        var fromPostion = spots.indexOf(pickUpSpot)
        var toPosition = spots.indexOf(destinationSpot)

        destinationMatrix!![fromPostion][toPosition] = destinationMatrix!![fromPostion][toPosition] + 1

    }

    /**
     *
     * Adds a [Spot] to the collection of spots in the environment.
     * @return the new size of the spots.
     *
     */
    private fun addSpot(spot: Spot): MutableList<Spot> {
        spots.add(spot)
        return spots
    }

    /**
     *
     * Loops through the given array af [Spot] and calls the [addSpot] method to add the spots to the collection
     *
     */
    fun addSpots(spots: Array<Spot>) {
        for (spot in spots) {
            addSpot(spot)
        }
    }

    /**
     *
     * Adds a [EVtol] to the collection of evtols in the environment.
     * @return the new size of the evtols.
     *
     */
    private fun addEVtol(evtol: EVtol): MutableList<EVtol> {
        evtols.add(evtol)
        return evtols
    }

    /**
     *
     * Loops through the given array af [EVtol] and calls the [addEVtol] method to add the evtols to the collection
     *
     */
    fun addEVtols(evtols: Array<EVtol>) {
        for (evtol in evtols) {
            addEVtol(evtol)
        }
    }

    fun getSpotGraphForPassenger(passenger: Passenger): HashMap<Spot, Spot> {
        var fromPostion = spots.indexOf(passenger.pickUpSpot)
        var destinationsMap: HashMap<Spot, Spot> = HashMap()

        distanceMatrix?.let {
            destinationsMap = dijkstra(fromPostion, distanceMatrix!!, spots.size)
        }

        // println("Passenger travels from: " + passenger.pickUpSpot!!.name + " to: " + passenger.destinationSpot.name + " through: ")
        // for ((key, value) in destinationsMap) {
        //    println("${key.name} = ${value.name}")
        // }
        return destinationsMap
    }

    private fun dijkstra(source: Int, edges: Array<DoubleArray>, nodes: Int): HashMap<Spot, Spot> {
        // Initialize single source
        val distances = DoubleArray(nodes) { Double.MAX_VALUE }
        distances[source] = 0.0

        val nodesToVisit: MutableList<Int> = (0 until nodes).toMutableList()
        val destinationsMap: HashMap<Spot, Spot> = HashMap()

        // Iterations
        while (nodesToVisit.isNotEmpty()) {
            val u: Int = extractMin(nodesToVisit, distances)

            edges[u].forEachIndexed { v, vd ->
                if (vd != -1.0 && distances[v] > distances[u] + vd) {
                    distances[v] = distances[u] + vd
                    destinationsMap.put(spots.get(v), spots.get(u))
                }
            }
        }

        return destinationsMap
    }


    private fun extractMin(nodesToVisit: MutableList<Int>, d: DoubleArray): Int {
        var minNode = nodesToVisit[0]
        var minDistance: Double = d[0]

        nodesToVisit.forEach {
            if (d[it] < minDistance) {
                minNode = it
                minDistance = d[it]
            }
        }

        nodesToVisit.remove(minNode)
        return minNode
    }

    fun getNextSpotForPassenger(
        destinationMap: HashMap<Spot, Spot>,
        pickUpSpot: Spot,
        destinationSpot: Spot
    ): Spot? {
        return if (destinationMap.get(destinationSpot)!!.equals(pickUpSpot)) {
            destinationSpot
        } else destinationMap.get(destinationSpot)?.let { getNextSpotForPassenger(destinationMap, pickUpSpot, it) }
    }
}