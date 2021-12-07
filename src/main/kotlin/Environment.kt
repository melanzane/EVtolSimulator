/**
 * Environment is an object which controls all other class objects.
 *
 * There can only be one environment at a runtime.
 */
object Environment {

    val spots: MutableList<Spot> = mutableListOf()

    val evtols: MutableList<EVtol> = mutableListOf()

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
     *
     * Adds a [Spot] to the collection of spots in the environment.
     * @return the new size of the spots.
     *
     */
    fun addSpot(spot: Spot): MutableList<Spot> {
        spots.add(spot)
        return spots
    }

    /**
     *
     * Adds an array af [Spot] to the collection of spots in the environment.
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
    fun addEVtols(evtol: EVtol): MutableList<EVtol> {
        evtols.add(evtol)
        return evtols
    }
}