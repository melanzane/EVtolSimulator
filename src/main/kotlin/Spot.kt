/**
 * Spots are class objects definining a eVtol bay
 *
 * Every stop has a charging station, which
 * can either be a supercharger or a normal charger
 *
 */
class Spot(var name: String, var chargingCapacity: Charger, var coordinates: Map<String, Double>) {
    val passengers: MutableList<Passenger> = mutableListOf()

    val position = coordinates

    /**
     *
     * Adds a [Passenger] to the collection passengers at the spot.
     * @return the new size of the passengers.
     *
     */
    fun updatePassengers(passenger: Passenger): MutableList<Passenger> {

        passengers.add(passenger)
        return passengers
    }

}