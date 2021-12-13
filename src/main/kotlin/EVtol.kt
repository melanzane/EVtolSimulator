
class EVtol(var identifier: Int) {
    private val position = mutableMapOf(Constants.LATITUDE to 0.00, Constants.LONGITUDE to 0.00)
    var passengers = arrayOfNulls<Passenger>(Constants.MAX_PASSENGERS_PER_EVTOL)
    var batteryCapacity: Int = Constants.MAX_BATTERY_CAPACITY
    var speed: Double = 0.00
    var altitute: Double = 0.00
    var range: Double = Constants.MAX_RANGE

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

    fun updateSpeed(speed: Double): Double {

        try {
            if (speed <= 0.00 || speed >= Constants.MAX_SPEED) {
                this.speed = Constants.MAX_SPEED
                throw SpeedOutOfRangeException("The speed of $speed km/h is not valid. The speed will be set automatically to max speed of ${Constants.MAX_SPEED} km/h")
            } else {
                this.speed = speed
            }
        } catch (e: SpeedOutOfRangeException) {
            println(e.message)
        } catch (e: Exception) {
            println(e.message)
        }
        return this.speed
    }


    fun updateAltitude(altitude: Double): Double {
        this.altitute = altitute
        return altitude
    }
}