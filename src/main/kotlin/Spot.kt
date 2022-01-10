/**
 * Spots are class objects definining a eVtol bay
 *
 * Every stop has a charging station, which
 * can either be a supercharger or a normal charger
 *
 */
class Spot(var name: String, var chargingCapacity: Charger, var coordinates: Map<String, Double>) {
    var passengers: MutableList<Passenger> = mutableListOf()

    //  private var eVtol: EVtol? = null

    //TODO
    var eVtols: MutableList<EVtol> = mutableListOf()

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

    /**fun setEvtol(eVtol: EVtol?) {
    this.eVtol = eVtol
    }

    fun getEvtol(): EVtol? {
    return this.eVtol
    } */

    fun addEvtolsToSpot(evtol: EVtol): MutableList<EVtol> {
        eVtols.add(evtol)
        return eVtols
    }

    fun getEvtols(): MutableList<EVtol> {
        return eVtols
    }

    fun chargeEVtol(evtol: EVtol) {
        if (chargingCapacity.equals(Charger.SUPERCHARGER)) {
            evtol.charge(Constants.EVTOL_CHARGING_UNIT_SUPERCHARGER)
        } else if (chargingCapacity.equals((Charger.NORMALCHARGER))) {
            evtol.charge(Constants.EVTOL_CHARGING_UNIT_NORMALCHARGER)
        }
    }

}