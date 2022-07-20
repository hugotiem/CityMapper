package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.Marker

class MapViewModel: ViewModel() {
    var markersLiveData = MutableLiveData<List<Marker>>()
}