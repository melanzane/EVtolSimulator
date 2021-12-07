class EVtol(var identifier: Int) {
    private val position = mutableMapOf(Constants.LATITUDE to 0.00, Constants.LONGITUDE to 0.00)
    var passengers = arrayOfNulls<Passenger>(4)
    var batteryCapacity: Int = 100
    var speed: Double = 0.00
    var altitute: Double = 0.00
    var range: Double = 40.00

    /**
     * The position defines the current location of the evtol.
     * Its constantly updated
     *
     * @return the current position.
     */
    fun updatePosition(longitude: Double, latitude: Double): Map<String, Double> {
        position.put(Constants.LATITUDE, latitude)
        position.put(Constants.LONGITUDE, longitude)
        return position
    }

    fun updatePassengers(passengers: Array<Passenger>): Array<Passenger> {
        var index = 0
        for (passenger in passengers) {
            this.passengers[index] = passenger
            index = +1
        }
        return passengers
    }

    fun updateBatteryCapacity(capacity: Int): Int {
        this.batteryCapacity = capacity
        return batteryCapacity
    }
}