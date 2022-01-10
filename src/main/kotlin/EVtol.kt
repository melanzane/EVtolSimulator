class EVtol(var identifier: Int) {
    private val position = mutableMapOf(Constants.LATITUDE to 0.00, Constants.LONGITUDE to 0.00)
    var passengers = arrayOfNulls<Passenger>(Constants.MAX_PASSENGERS_PER_EVTOL)
    private var batteryCapacity: Int = Constants.MAX_BATTERY_CAPACITY
    private var speed: Double = 0.00
    private var altitute: Double = 0.00
    private var destinationSpot: Spot? = null

    /**
     * The position defines the current location of the evtol.
     * Its constantly updated
     *
     * @return the current position.
     */
    fun updatePosition(longitude: Double, latitude: Double): Map<String, Double> {
        if (isValidLatitudeAndLongitude(longitude, latitude)) {
            position.put(Constants.LATITUDE, latitude)
            position.put(Constants.LONGITUDE, longitude)
        }
        return position
    }

    private fun isValidLatitudeAndLongitude(longitude: Double?, latitude: Double?): Boolean {
        return latitude?.toInt() in -90 until 90 && longitude?.toInt() in -180 until 180
    }

    fun getCurrentPosition(): Map<String, Double> {
        return this.position
    }

    fun updatePassengers(passengers: Array<Passenger>): Array<Passenger?> {
        for (i in 0..Constants.MAX_PASSENGERS_PER_EVTOL - 1) {
            this.passengers[i] = passengers.get(i)
        }
        return this.passengers
    }

    /**
     * Calculates the battery capacity needed for a distance and distracts this value from the current battery capacity
     *  @returns the current battery capacity of the eVtol.
     */
    fun updateBatteryCapacity(distanceToFly: Double): Int {
        var capacityNeededForGivenDistance = (100 / Constants.MAX_BATTERY_CAPACITY) * distanceToFly
        this.batteryCapacity = this.batteryCapacity - capacityNeededForGivenDistance.toInt()
        return batteryCapacity
    }

    /**
     * Calculates the battery capacity after charging an eVtol at a spot.
     *  @returns the current battery capacity of the eVtol.
     */
    fun charge(chargedCapatiy: Int): Int {
        if ((this.batteryCapacity + chargedCapatiy) > Constants.MAX_BATTERY_CAPACITY) {
            this.batteryCapacity = Constants.MAX_BATTERY_CAPACITY
        } else {
            this.batteryCapacity = this.batteryCapacity + chargedCapatiy
        }
        return batteryCapacity
    }

    /**
     * Updates the speed in km/h
     * Adjusts the speed to max speed if the value is too high or too low
     *  @returns the speed of the eVtol
     */
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

    fun isCharged(): Boolean {
        return batteryCapacity == Constants.MAX_BATTERY_CAPACITY
    }

    fun setDestinationSpot(spot: Spot?) {
        this.destinationSpot = spot
    }

    fun getDestinationSpot(): Spot? {
        return this.destinationSpot
    }

    /**
     * Sets the speed in km/h
     */
    fun setSpeed(speed: Double) {
        this.speed = speed
    }

    fun getSpeed(): Double {
        return this.speed
    }

    /**
     * Sets the altitude in meters
     */
    fun setAltitude(altitude: Double) {
        this.altitute = altitute
    }

    fun getAltitude(): Double {
        return this.altitute
    }

    fun getBatteryCapacity(): Int {
        return this.batteryCapacity
    }

}