package fr.hugotiem.citymapper.viewModel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import fr.hugotiem.citymapper.model.TravelMode
import fr.hugotiem.citymapper.providers.SearchProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.io.IOException
import java.io.Serializable


class SearchViewModel: ViewModel() {

    val lastSearchLiveData: MutableLiveData<List<AutocompleteItems>> = MutableLiveData<List<AutocompleteItems>>()

    val autocompleteLiveData: MutableLiveData<List<AutocompleteItems>> = MutableLiveData<List<AutocompleteItems>>()

    suspend fun getAutocomplete(search: String): Unit = coroutineScope {
        if(search.isEmpty() || search.isNullOrBlank()) {
            Log.d("ERROR", "Empty")
        } else {
            launch {
                var res = SearchProvider.service.getResults(search)

                if(res.isSuccessful) {
                    val content = res.body()
                    val gson = Gson()
                    val data: Map<String, *> = gson.fromJson(content?.string(), Map::class.java) as Map<String, *>
                    autocomplete = AutocompleteItems.fromJson(data)
                    //Log.d("Autocomplete:success", content?.string() ?: "Empty")
                    //Log.d("Autocomplete:success", items[0].name ?: "Empty")
                }
            }
        }
    }

    var historical: List<AutocompleteItems>
        get() = lastSearchLiveData.value ?: listOf()
        set(value) {
            lastSearchLiveData.postValue(value)
        }

    var autocomplete: List<AutocompleteItems>
        get() = autocompleteLiveData.value ?: listOf()
        set(value) {
            autocompleteLiveData.postValue(value)
        }


    fun getLocationFromAddress(context: Context?, strAddress: String?): LatLng? {
        val coder = Geocoder(context)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.getLatitude(), location.getLongitude())

            Log.d("LATLNG", location.latitude.toString())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }
}

class AutocompleteItems(val name: String) {

    companion object {
        fun fromJson(json: Map<String, *>) : List<AutocompleteItems> {
            val predictions = json["predictions"] as ArrayList<Map<String, *>>

            val results = predictions.map { prediction ->
                Log.d("TEST", prediction.toString())
                AutocompleteItems(prediction["description"] as String)
            }
            return results
        }
    }
}

@Parcelize
class ParcelableSearch(val travelMode: TravelMode, val startLatLng: ParcelableCoordinates, val endLatLng: ParcelableCoordinates, val destinationName: String) : Parcelable

@Parcelize
class ParcelableCoordinates(val lat: Double, val long: Double) : Parcelable