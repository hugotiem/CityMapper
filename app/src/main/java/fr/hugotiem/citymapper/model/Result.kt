package fr.hugotiem.citymapper.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

//open class Result(var time: Int)

//class TransitResult(time: Int, var price: Double, var arrivedAt: Date, var steps: List<String>) : Result(time) {}

//class DrivingResult(time: Int): Result(time) {}

class Result(

    val bounds: List<LatLng>,
    val price: Float,
    // leg = une possibilté de trajet
    val legs: List<Leg>
)

class Leg(
    // pour la class results => seulement retourner le type de transport et
    // le numero du metro/bus/tram utilisé si c'est en transit
    val steps: List<Step>,
    val duration: Int,
    val distance: Int,
    val startLatLng: LatLng,
    val endLatLng: LatLng,
    val travelMode: TravelMode // peut être retourné en String par le back puis
    // parsé en enum dans le front
)

enum class TravelMode { transit, walking, driving }

// utile pour les polylines
open class Step(
    val duration: Int,
    val distance: Int,
    val startLatLng: LatLng,
    val endLatLng: LatLng,
    val instructions: String,
    val maneuver: Unit // ?
)

class TransitStep(
    val transitDetail: TransitDetail,
    duration: Int,
    distance: Int,
    startLatLng: LatLng,
    endLatLng: LatLng,
    instructions: String,
    maneuver: Unit
): Step(duration, distance, startLatLng, endLatLng, instructions, maneuver)


class TransitDetail(
    val name: String,
    val departureStop: String,
    val destinationStop: String,
)