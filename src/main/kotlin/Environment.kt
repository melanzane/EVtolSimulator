/**
 * Environment is an object which controls all other class objects.
 *
 * There can only be one environment at a runtime.
 */
object Environment {

    var spots: MutableList<Spot> = mutableListOf()

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
    private fun distanceBetweenLocationsInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
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

        this.destinationMatrix = Array(spots.size) { IntArray(spots.size) }

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

    fun getNextPossibleSpotForPassenger(passenger: Passenger): HashMap<Spot, Spot> {
        var fromPostion = spots.indexOf(passenger.pickUpSpot)
        var destinationsMap: HashMap<Spot, Spot> = HashMap()

        distanceMatrix?.let {
            destinationsMap = dijkstra(fromPostion, distanceMatrix!!, spots.size)
        }

        println("Passenger travels from: " + passenger.pickUpSpot!!.name + " to: " + passenger.destinationSpot.name + " through: ")
        for ((key, value) in destinationsMap) {
            println("${key.name} = ${value.name}")
        }
        return destinationsMap
    }

    private fun dijkstra(source: Int, edges: Array<DoubleArray>, nodes: Int): HashMap<Spot, Spot> {
        // Initialize single source
        val d = DoubleArray(nodes) { Double.MAX_VALUE }
        val pi = IntArray(nodes) { -1 }
        d[source] = 0.0

        val Q: MutableList<Int> = (0 until nodes).toMutableList()
        val destinationsMap: HashMap<Spot, Spot> = HashMap()

        // Iterations
        while (Q.isNotEmpty()) {
            val u: Int = extractMin(Q, d)

            edges[u].forEachIndexed { v, vd ->
                if (vd != -1.0 && d[v] > d[u] + vd) {
                    d[v] = d[u] + vd
                    pi[v] = u
                    destinationsMap.put(spots.get(v), spots.get(u))
                }
            }
        }

        println("d: ${d.contentToString()}")
        return destinationsMap
    }


    private fun extractMin(Q: MutableList<Int>, d: DoubleArray): Int {
        var minNode = Q[0]
        var minDistance: Double = d[0]

        Q.forEach {
            if (d[it] < minDistance) {
                minNode = it
                minDistance = d[it]
            }
        }

        Q.remove(minNode)
        return minNode
    }
}