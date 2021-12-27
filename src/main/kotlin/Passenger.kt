/**
 * Passengers are class objects defining people travelling fron one destination to another.
 *
 * Every passenger has a pick up destination and a target destination.
 * The destinations are mappen by the [Spot] class
 */
class Passenger(var name: String, var pickUpSpot: Spot?, var destinationSpot: Spot) {

    private val position = mutableMapOf(Constants.LATITUDE to 0.00, Constants.LONGITUDE to 0.00)
    private val destinationsMap: MutableMap<Spot, Spot> = HashMap<Spot, Spot>().toMutableMap()


    /**
     * The position defines the current location of the passenger.
     * Its constantly updated
     *
     * @return the current position.
     */
    fun updatePosition(longitude: Double, latitude: Double): Map<String, Double> {
        position.put(Constants.LATITUDE, latitude)
        position.put(Constants.LONGITUDE, longitude)
        return position
    }

    /**
     * Each passenger has a pickUpSpot and a destinationSpot.
     * If the destinationSpot can't be reached with one flight,
     * the passenger is a assigned a map with one or more destinations in between.
     *
     * @return the map with all the shortest destinations from the pickUpSpot
     */
    fun updateDestinationMap(destinationMap: HashMap<Spot, Spot>): MutableMap<Spot, Spot> {
        this.destinationsMap.putAll(destinationMap)
        return this.destinationsMap
    }
}



