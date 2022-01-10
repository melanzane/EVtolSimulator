import java.util.*
import kotlin.concurrent.schedule
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
                mutableMapOf("latitude" to 37.4249706, "longitude" to -122.1878931)
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

            val southSanJose = Spot(
                "South San Jose",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.264833, "longitude" to -121.8894972)
            )

            val morganHill = Spot(
                "Morgan Hill",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.1290233, "longitude" to -121.6543579)
            )

            val sanMartin = Spot(
                "San Martin",
                Charger.SUPERCHARGER,
                mutableMapOf("latitude" to 37.0926408, "longitude" to -121.6244688)
            )


            //create EVtols
            val eVtol1 = EVtol(1)

            val eVtol2 = EVtol(2)

            val eVtol3 = EVtol(3)

            val eVtol4 = EVtol(4)

            val eVtol5 = EVtol(5)


            //create the environment
            // Add all the spots to the environment
            // San Francisco, Redwood City, East Palo Alto, Palo Alto, Cupertino, San Jose
            Environment.addSpots(
                arrayOf(
                    sanFrancisco,
                    redwoodCity,
                    eastPaloAlto,
                    cupertino,
                    sanJose,
                    paloAlto,
                    southSanJose,
                    morganHill,
                    sanMartin
                )
            )

            //create a matrix with distances from and to all spots
            Environment.createDistanceMatrix()

            //create a matrix with destinations for the passengers
            Environment.createDestinationMatrix()


            //Add eVtols to the environment
            Environment.addEVtols(arrayOf(eVtol1, eVtol2, eVtol3, eVtol4, eVtol5))

            // generate random passengers and add them to spot
            val numberOfPassengers = 60
            generateRandomPassengers(numberOfPassengers)

            // add eVtols to random spots
            updateVolsWithSpots(arrayOf(eVtol1, eVtol2, eVtol3, eVtol4, eVtol5))

            // play the simulation by picking up the passengers at the spots and flying them
            // to their destinations
            playSimulation()

        }

        private fun playSimulation() {

            for (passenger in Environment.passengers) {
                var distanceToTravel =
                    Environment.getDistanceBetweenSpotsInKm(passenger.pickUpSpot!!, passenger.destinationSpot)
                if ((distanceToTravel != null) && (distanceToTravel <= Constants.MAX_RANGE)) {
                    Environment.addPassengerToDestinationMatrix(
                        passenger.pickUpSpot!!,
                        passenger.destinationSpot
                    )
                    passenger.pickUpSpot = null
                } else {
                    var destinationMap = Environment.getSpotGraphForPassenger(passenger)
                    passenger.updateDestinationMap(destinationMap)
                    var nextSpot =
                        Environment.getNextSpotForPassenger(
                            destinationMap,
                            passenger.pickUpSpot!!,
                            passenger.destinationSpot
                        )
                    if (nextSpot != null) {
                        Environment.addPassengerToDestinationMatrix(passenger.pickUpSpot!!, nextSpot)
                        passenger.pickUpSpot = nextSpot

                    }
                }
            }

            // loop through spots and check for passengers and evtols.
            // if there are more than two passengers and a charged evtols, the evtols is send to fly
            for (spot in Environment.spots) {
                var index = Environment.spots.indexOf(spot)
                val chargedEvtolsAtSpot = spot.eVtols.filter { it.isCharged() }
                if (!chargedEvtolsAtSpot.isEmpty()) {
                    if (Environment.destinationMatrix?.get(index)?.any { Int -> Int >= 2 } == true) {
                        flyEvtolToNextSpot(index, spot, chargedEvtolsAtSpot.first())
                    }
                }
            }

            Environment.passengers.removeIf { x -> x.pickUpSpot == null }

            updateEvtolsInTheAir()

            updateEvtolsOnGround()
            Timer().schedule(3000) {
                playSimulation()
            }

        }

        private fun updateEvtolsOnGround() {
            for (spot in Environment.spots) {
                val unchargedEvtolsAtSpot = spot.eVtols.filter { !it.isCharged() }
                if (!unchargedEvtolsAtSpot.isEmpty()) {
                    for (unchargedEvtol in unchargedEvtolsAtSpot) {
                        spot.chargeEVtol(unchargedEvtol)
                        println("Charging evtol nr. " + unchargedEvtol.identifier + " at the spot " + spot.name + ". The battery capacity is at: " + unchargedEvtol.batteryCapacity + " percent.")
                    }
                }
            }
        }
    }
}

//TODO: Think about the nutzlast! Less passengers means longer range!
private fun updateEvtolsInTheAir() {
    for (evtol in Environment.evtols) {
        if (evtol.getDestinationSpot() != null) {
            var destinationSpot = evtol.getDestinationSpot()!!
            var distanceToDestination = Environment.distanceBetweenLocationsInKm(
                evtol.getCurrentPosition().get(Constants.LATITUDE)!!,
                evtol.getCurrentPosition().get(Constants.LONGITUDE)!!,
                destinationSpot.coordinates.get(Constants.LATITUDE)!!,
                destinationSpot.coordinates.get(Constants.LONGITUDE)!!
            )
            // if the distance left to fly is over 13.00 km, the eVtol keeps the average speed and altitude
            if (distanceToDestination >= 13.00) {
                evtol.batteryCapacity = evtol.updateBatteryCapacity(distanceToDestination)
                evtol.setSpeed(Constants.EVTOL_AVERAGE_SPEED)
                evtol.setAltitude(Constants.EVTOL_AVERAGE_ALTITUDE)
                var bearing = Environment.getBearing(
                    evtol.getCurrentPosition().get(Constants.LATITUDE)!!,
                    evtol.getCurrentPosition().get(Constants.LONGITUDE)!!,
                    destinationSpot.coordinates.get(Constants.LATITUDE)!!,
                    destinationSpot.coordinates.get(Constants.LONGITUDE)!!
                )
                var points = Environment.getPointByDistanceAndBearing(
                    evtol.getCurrentPosition().get(Constants.LATITUDE)!!,
                    evtol.getCurrentPosition().get(Constants.LONGITUDE)!!, bearing, 13.0
                )

                evtol.updatePosition(points.second, points.first)

                println(
                    "Flying Vtol nr. " + evtol.identifier + " to: " + evtol.getDestinationSpot()!!.name + ". The current position is: "
                            + points.first + " , " + points.second
                )
                println("              -----|-----")
                println("           *>=====[_]L)")
                println("                 -'-`-")
                println("")

            }
            // descending flight
            else {
                evtol.updateBatteryCapacity(distanceToDestination)
                evtol.updateSpeed(Constants.EVTOL_LANDING_SPEED)
                evtol.updateAltitude(Constants.EVTOL_LANDING_ALTITUDE)
                Environment.spots.get(Environment.spots.indexOf(evtol.getDestinationSpot())).addEvtolsToSpot(evtol)
                var bearing = Environment.getBearing(
                    evtol.getCurrentPosition().get(Constants.LATITUDE)!!,
                    evtol.getCurrentPosition().get(Constants.LONGITUDE)!!,
                    destinationSpot.coordinates.get(Constants.LATITUDE)!!,
                    destinationSpot.coordinates.get(Constants.LONGITUDE)!!
                )
                var points = Environment.getPointByDistanceAndBearing(
                    evtol.getCurrentPosition().get(Constants.LATITUDE)!!,
                    evtol.getCurrentPosition().get(Constants.LONGITUDE)!!, bearing, 13.0
                )

                evtol.updatePosition(points.second, points.first)
                println(
                    "Flying Vtol nr. " + evtol.identifier + " to: " + evtol.getDestinationSpot()!!.name + ". The current position is: "
                            + points.first + " , " + points.second + " and the destination will be reached in about five minutes. "
                )
                println("                         -----|-----")
                println("                      *>=====[_]L)")
                println("                            -'-`-")
                println("")
                evtol.setDestinationSpot(null)

            }

        }
    }
}

private fun flyEvtolToNextSpot(index: Int, spot: Spot, eVtol: EVtol) {

    var destinationListFromCurrentSpot = Environment.destinationMatrix!!.get(index)
    var possibleDestinationsSpots = (destinationListFromCurrentSpot.filter { Int -> Int >= 2 }).size
    if (possibleDestinationsSpots > 1) {
        //check for passengers in destinationspot
        for (spotIndex in destinationListFromCurrentSpot) {
            if (destinationListFromCurrentSpot[spotIndex] >= 2) {
                var destinationListFromFromPossibleDestination = Environment.destinationMatrix!!.get(spotIndex)
                var possibleDestinationsSpotsOfNextDestinaion =
                    (destinationListFromFromPossibleDestination.filter { Int -> Int >= 2 }).size
                if (possibleDestinationsSpotsOfNextDestinaion >= 0) {
                    var passengersOnBoard = Environment.adjustDestinationMatrixAfterAscending(index, spotIndex)
                    startAscendingFlight(spotIndex, eVtol, spot, passengersOnBoard)
                }
            }
        }
    } else {
        var destinationSpotIndex =
            destinationListFromCurrentSpot.indexOf(destinationListFromCurrentSpot.first { it >= 2 })
        var passengersOnBoard = Environment.adjustDestinationMatrixAfterAscending(index, destinationSpotIndex)
        startAscendingFlight(destinationSpotIndex, eVtol, spot, passengersOnBoard)
    }
    spot.getEvtols().remove(eVtol)

}

private fun startAscendingFlight(
    spotIndex: Int,
    eVtol: EVtol,
    spot: Spot,
    numberOfPassenger: Int
) {
    eVtol.setDestinationSpot(Environment.spots[spotIndex])
    eVtol.setSpeed(Constants.EVTOL_TAKE_OFF_SPEED)
    eVtol.setAltitude(Constants.EVTOL_TAKE_OFF_ALTITUDE)
    println(
        "Flying Vtol nr. " + eVtol.identifier + " from: " + spot.name + " to: " + eVtol.getDestinationSpot()!!.name + " with " +

                numberOfPassenger + " passengers."
    )
    println("   -----|-----")
    println("*>=====[_]L)")
    println("      -'-`-")
    println("")
}


private fun updateVolsWithSpots(evtols: Array<EVtol>) {
    for (evtol in evtols) {
        val spot = Random.nextInt(Environment.spots.size)
        Environment.spots[spot].position.get(Constants.LONGITUDE)
            ?.let {
                Environment.spots[spot].position.get(Constants.LATITUDE)
                    ?.let { it1 -> evtol.updatePosition(it, it1) }
            }
        Environment.spots[spot].addEvtolsToSpot(evtol)
    }
}


private fun generateRandomPassengers(numberOfPassengers: Int) {

    for (i in 0..numberOfPassengers) {
        val passengersSpots = generateTwoRandomSpots()
        Environment.passengers.add(Passenger(getRandomName(5), passengersSpots[0], passengersSpots[1]))

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




