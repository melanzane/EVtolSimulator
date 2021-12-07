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

            // create passengers and add them to spots
            val passengerJohn = Passenger("John", sanFrancisco, sanJose)
            sanFrancisco.updatePassengers(passengerJohn)

            val passengerLeo = Passenger("Leo", sanJose, sanFrancisco)
            sanJose.updatePassengers(passengerLeo)

            val passengerAnna = Passenger("Anna", sanJose, paloAlto)
            sanJose.updatePassengers(passengerAnna)

            val passengerLouise = Passenger("Louise", sanJose, paloAlto)
            sanJose.updatePassengers(passengerLouise)

            val passengerChris = Passenger("Louise", paloAlto, sanFrancisco)
            paloAlto.updatePassengers(passengerChris)


            //create the environment
            Environment.addSpots(arrayOf(sanFrancisco, sanJose, paloAlto))
            for (i in sanJose.passengers) {
                println(i.name)
            }
            println(Environment.getDistanceBetweenSpotsInKm(sanFrancisco, paloAlto))


        }

    }


}