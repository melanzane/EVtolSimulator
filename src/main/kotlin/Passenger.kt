/**
 * Passengers are class objects defining people travelling fron one destination to another.
 *
 * Every passenger has a pick up destination and a target destination.
 * The destinations are mappen by the [Spot] class
 */
class Passenger(var name: String, var pickUpSpot: Spot, var destinationSpot: Spot) {

    private val position = mutableMapOf(Constants.LATITUDE to 0.00, Constants.LONGITUDE to 0.00)

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
    }}



