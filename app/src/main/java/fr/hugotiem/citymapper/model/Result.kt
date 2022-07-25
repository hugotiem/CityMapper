package fr.hugotiem.citymapper.model

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import org.json.JSONObject
import java.util.*
import kotlin.Result

//open class Result(var time: Int)

//class TransitResult(time: Int, var price: Double, var arrivedAt: Date, var steps: List<String>) : Result(time) {}

//class DrivingResult(time: Int): Result(time) {}

class ApiResult(

    val bounds: List<LatLng>,
    // leg = une possibilté de trajet
    val legs: List<Leg>
) {
    companion object {
        fun fromJson(json: List<Map<String, *>>): ApiResult {
            Log.d("ApiResult:fromJson", json.toString())
            return ApiResult(bounds = listOf(), legs = json.map { e -> Leg.fromJson(e) })
        }
    }
}

class Leg(
    // pour la class results => seulement retourner le type de transport et
    // le numero du metro/bus/tram utilisé si c'est en transit
    val steps: List<Step>,
    val price: Double,
    val duration: Int,
    val distance: Int?,
    val startLatLng: LatLng?,
    val endLatLng: LatLng?,
    val travelMode: TravelMode // peut être retourné en String par le back puis
    // parsé en enum dans le front
) {
    companion object {
        fun fromJson(json: Map<String, *>): Leg {
            val price = json["price"] as Double
            val steps = json["steps"] as List<Map<String, *>>
            return Leg(
                price = price,
                steps = steps.map { step ->
                    if(step["type"] == "public_transport") {
                        TransitStep.fromJson(step)
                    } else {
                        Step.fromJson(step)
                    }
                },
                duration = (json["routeDuration"] as Double).toInt(),
                distance = null,
                startLatLng = null,
                endLatLng = null,
                travelMode = TravelMode.transit
            )
        }
    }
}

enum class TravelMode { transit, walking, driving }

// utile pour les polylines
open class Step(
    val duration: Int? = null,
    val distance: Int? = null,
    val startLatLng: LatLng? = null,
    val endLatLng: LatLng? = null,
    val polyline: List<LatLng>
) {
    companion object {
        fun fromJson(json: Map<String, *>): Step {
            return Step(polyline = polylineFactory(json["geoJson"] as List<Map<String, Double>>))
        }

        fun polylineFactory(list:List<Map<String, Double>>): List<LatLng> {
            return list.map { item -> latLngFactory(item) }
        }

        fun latLngFactory(map: Map<String, Double>): LatLng {
            return LatLng(map["lat"] as Double, map["long"] as Double)
        }
    }
}

class TransitStep(
    val transitDetail: TransitDetail,
    duration: Int,
    distance: Int?,
    startLatLng: LatLng?,
    endLatLng: LatLng?,
    polyline: List<LatLng>
): Step(duration, distance, startLatLng, endLatLng, polyline) {
    companion object  {
        fun fromJson(json: Map<String, *>): TransitStep {

            val transportType: TransportType

            when(json["transportType"]) {
                "Bus" -> transportType = TransportType.BUS
                "Metro" -> transportType = TransportType.METRO
                else -> transportType = TransportType.TRAIN
            }

            val polyline = polylineFactory(json["geoJson"] as List<Map<String, Double>>)

            return TransitStep(
                TransitDetail(
                    json["transportName"] as String,
                    json["transportColor"] as String,
                    transportType = transportType
                ),
                duration = (json["duration"] as Double).toInt(),
                startLatLng = null,
                endLatLng = null,
                distance = null,
                polyline = polyline
            )
        }
    }
}


class TransitDetail(
    val name: String,
    val color: String,
    val transportType: TransportType
)

enum class TransportType { BUS, METRO, TRAIN }


