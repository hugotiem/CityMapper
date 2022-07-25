package fr.hugotiem.citymapper.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.datatransport.runtime.Destination
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import fr.hugotiem.citymapper.model.ApiResult
import fr.hugotiem.citymapper.model.Leg
import fr.hugotiem.citymapper.model.TravelMode
import fr.hugotiem.citymapper.providers.ApiProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.Serializable

enum class ScheduleType { START , END }

class ResultsViewModel: ViewModel() {

    lateinit var startLatLng: LatLng
    lateinit var endLatLng: LatLng
    lateinit var travelMode: TravelMode
    lateinit var destinationName: String

    val resultsLiveData: MutableLiveData<ApiResult> = MutableLiveData<ApiResult>()
    val selectedLiveData: MutableLiveData<Leg> = MutableLiveData<Leg>()

    fun stringType(type: ScheduleType): String {
        if(type == ScheduleType.START) {
            return "Départ"
        }
        return "Arrivée"
    }

    fun initPage(startLatLng: LatLng, endLatLng: LatLng, travelMode: TravelMode, destinationName: String) {
        this.startLatLng = startLatLng
        this.endLatLng = endLatLng
        this.travelMode = travelMode
        this.destinationName = destinationName
    }

    suspend fun searchItineraries(
        startLatLng: LatLng = this.startLatLng,
        endLatLng: LatLng = this.endLatLng,
        travelMode: TravelMode = this.travelMode
    ): Unit = coroutineScope {

        val resultBody = ResultBody(
            travelMode,
            startLatLng = Coordinates(
                lat = startLatLng.latitude,
                long = startLatLng.longitude
            ),
            endLatLng = Coordinates(
                lat = endLatLng.latitude,
                long = endLatLng.longitude
            )
        )

        launch {
            val res = ApiProvider.service.getResults(resultBody)

            if(res.isSuccessful) {
                var body = res.body()

                val json: List<Map<String, *>> = Gson().fromJson(body?.string(), List::class.java) as List<Map<String, *>>
                val result = ApiResult.fromJson(json)
                resultsLiveData.value = result
               // val data: Map<String, *> = Gson().fromJson(body?.string(), Map::class.java) as Map<String, *>
                Log.d("RESPONSE", result.legs[0].price.toString() ?: "no response")
            }
        }
    }
}


class ResultBody(val travelMode: TravelMode, val startLatLng: Coordinates, val endLatLng: Coordinates) : Serializable

class Coordinates(val lat: Double, val long: Double) : Serializable