/**
 * Environment is an object which controls all other class objects.
 *
 * There can only be one environment at a runtime.
 */
object Environment {

    val spots: MutableList<Spot> = mutableListOf()

    val evtols: MutableList<EVtol> = mutableListOf()

    var matrix: Array<IntArray>? = null

    fun getDistanceBetweenSpotsInKm(fromSpot: Spot, toSpot: Spot): Double? {

        val distanceInKm = fromSpot.position.get(Constants.LATITUDE)
            ?.let {
                toSpot.position.get(Constants.LATITUDE)?.let { it1 ->
                    distanceInKm(
                        it, fromSpot.position.get(Constants.LONGITUDE)!!,
                        it1, toSpot.position.get(Constants.LONGITUDE)!!
                    )
                }
            }
        return distanceInKm
    }

    private fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist =
            Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(
                deg2rad(theta)
            )
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60 * 1.1515
        dist *= 1.609344
        return dist
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

    /**
     * Creates a matrix with destinations for each spot
     * The row is the "from" destination of a passenger and the column is the "to" destination of a passenger
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
    fun createDestinationMatrix() {
        val matrixSize = spots.size
        this.matrix = Array(matrixSize) { IntArray(matrixSize) }
        for (row in 0..matrix!!.size - 1) {
            for (column in 0..matrix!!.size - 1) {
                matrix!![row][column] = 0
            }
        }
    }

    /**
     * Adds to the count of the destinations in the matrix
     */
    fun addPassengerToDestinationMatrix(pickUpSpot: Spot, destinationSpot: Spot) {

        var fromPostion = spots.indexOf(pickUpSpot)
        var toPosition = spots.indexOf(destinationSpot)

        matrix!![fromPostion][toPosition] = matrix!![fromPostion][toPosition] + 1

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

    fun getNextPossibleSpotForPassenger(passenger: Passenger) {
        val destinationInKm = getDistanceBetweenSpotsInKm(passenger.pickUpSpot!!, passenger.destinationSpot)
        var closestSpotToCurrentDestination: Spot? = null

        if (destinationInKm != null) {
            while (destinationInKm >= Constants.MAX_RANGE) {

                val postionOfCurrentDestination = Environment.spots.indexOf(passenger.destinationSpot)
                if (postionOfCurrentDestination == 0) {
                    closestSpotToCurrentDestination = spots.get(postionOfCurrentDestination + 1)
                }
                if (postionOfCurrentDestination == spots.size - 1) {
                    closestSpotToCurrentDestination = spots.get(postionOfCurrentDestination - 1)
                } else {
                    var distanceFirstNeighbor = getDistanceBetweenSpotsInKm(
                        spots.get(postionOfCurrentDestination - 1),
                        passenger.destinationSpot
                    )
                    var distanceSecondNeighbor = getDistanceBetweenSpotsInKm(
                        spots.get(postionOfCurrentDestination - 1),
                        passenger.destinationSpot
                    )

                }
            }
        }
    }
}