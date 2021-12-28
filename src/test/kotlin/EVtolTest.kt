import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class EVtolTest {

    private val eVtol1: EVtol = EVtol(1)

    private val spot1: Spot = Spot(
        "San Francisco",
        Charger.NORMALCHARGER,
        mutableMapOf("latitude" to 37.7576793, "longitude" to -122.5076404)
    )

    private val spot2: Spot = Spot(
        "Redwood City",
        Charger.SUPERCHARGER,
        mutableMapOf("latitude" to 37.5083527, "longitude" to -122.2856248)
    )

    private val passenger1: Passenger = Passenger("passenger1", spot1, spot2)
    private val passenger2: Passenger = Passenger("passenger2", spot1, spot2)
    private val passenger3: Passenger = Passenger("passenger3", spot1, spot2)
    private val passenger4: Passenger = Passenger("passenger4", spot1, spot2)
    private val passenger5: Passenger = Passenger("passenger5", spot1, spot2)


    @Test
    fun getBatteryCapacity() {
    }

    @Test
    fun setBatteryCapacity() {
    }

    @Test
    fun getSpeed() {
    }

    @Test
    fun setSpeed() {
    }

    @Test
    fun getAltitute() {
    }

    @Test
    fun setAltitute() {
    }

    @Test
    fun getRange() {
    }

    @Test
    fun setRange() {
    }

    @Test
    fun updatePosition() {

        // valid coordinates should be set
        var positionOfEvtol1: Map<String, Double>? = null
        positionOfEvtol1 = eVtol1.updatePosition(-122.2856248, 37.5083527)

        //check that valid coordinates are set
        assertEquals(37.5083527, positionOfEvtol1!!.get(Constants.LATITUDE))

        // invalid coordinates shouldn't be set
        //Latitude must be a number between -90 and 90
        //Longitude must a number between -180 and 180
        positionOfEvtol1 = eVtol1.updatePosition(182.0, 84.1234)

        //check that invalid coordinates are not set and the previous postion remains
        assertEquals(37.5083527, positionOfEvtol1!!.get(Constants.LATITUDE))

        // valid coordinates should be set
        positionOfEvtol1 = eVtol1.updatePosition(-133.3356248, 47.4783527)
        
        //check that valid coordinates are set
        assertEquals(47.4783527, positionOfEvtol1!!.get(Constants.LATITUDE))

    }

    @Test
    fun updatePassengers() {
        var passengersList1 = eVtol1.updatePassengers(arrayOf(passenger1, passenger2, passenger3, passenger4))
        assertEquals(4, passengersList1.size)

        var passengersList2 =
            eVtol1.updatePassengers(arrayOf(passenger1, passenger2, passenger3, passenger4, passenger5))
        assertEquals(4, passengersList2.size)
    }

    @Test
    fun updateBatteryCapacity() {
    }

    @Test
    fun updateSpeed() {
    }

    @Test
    fun updateAltitude() {
    }

    @Test
    fun getIdentifier() {
        val expected = 1
        assertEquals(expected, eVtol1.identifier)
    }

}