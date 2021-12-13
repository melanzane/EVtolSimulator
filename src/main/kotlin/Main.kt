import kotlin.random.Random

public class Main {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            //create spots
            val sanFrancisco = Spot(
                "San Francisco",
                Charger.NORMALCHARGER,
                mutableMapOf("latitude" to 37.7576793, "longitude" to -122.5076404)
            )

            val redwoodCity = Spot(
                "Redwood City",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.5083527, "longitude" to -122.2856248)
            )
            val sanJose = Spot(
                "San Jose",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.2965315, "longitude" to -122.097605)
            )
            val paloAlto = Spot(
                "Palo Alto",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.4256293, "longitude" to -122.2053904)
            )

            val eastPaloAlto = Spot(
                "East Palo Alto",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.4679867, "longitude" to -122.1532526)
            )

            val cupertino = Spot(
                "Cupertino",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.3092448, "longitude" to -122.078664)
            )


            //create EVtols
            val eVtol1 = EVtol(1)

            val eVtol2 = EVtol(2)

            val eVtol3 = EVtol(3)


            //create the environment
            // Add all the spots to the environment
            // San Francisco, Redwood City, East Palo Alto, Palo Alto, Cupertino, San Jose
            Environment.addSpots(arrayOf(sanFrancisco, redwoodCity, eastPaloAlto, cupertino, sanJose, paloAlto))

            //Add eVtols to the environment
            Environment.addEVtols(arrayOf(eVtol1, eVtol2, eVtol3))

            val numberOfPassengers = 20
            generateRandomPassengers(numberOfPassengers)

            addPassengersToSpot(numberOfPassengers)


        }

        private fun addPassengersToSpot(numberOfPassengers: Int) {

            for (passenger in 0..numberOfPassengers) {

                for (spot in Environment.spots) {
                    if (passengers[passenger].pickUpSpot == spot) {
                        spot.updatePassengers(passengers[passenger])
                    }
                }

            }

        }

        private val passengers: MutableList<Passenger> = mutableListOf()

        private fun generateRandomPassengers(numberOfPassengers: Int) {

            for (i in 0..numberOfPassengers) {
                val passengersSpots = generateTwoRandomSpots()
                passengers.add(Passenger(getRandomName(5), passengersSpots[0], passengersSpots[1]))

            }

        }

        private fun generateTwoRandomSpots(): Array<Spot> {
            val pickUp = Random.nextInt(Environment.spots.size)
            val destination = Random.nextInt(Environment.spots.size);
            return if (pickUp != destination) {
                val spots = arrayOf(Environment.spots[pickUp], Environment.spots[destination])
                return spots
            } else {
                generateTwoRandomSpots()
            }

        }

        private fun getRandomName(length: Int): String {
            val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
            return (1..length)
                .map { charset.random() }
                .joinToString("")
        }

    }


}