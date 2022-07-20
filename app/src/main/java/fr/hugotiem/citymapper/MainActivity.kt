package fr.hugotiem.citymapper

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import fr.hugotiem.citymapper.ui.theme.CityMapperTheme
import fr.hugotiem.citymapper.viewModel.MapViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState

class MainActivity : ComponentActivity() {

    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapViewModel: MapViewModel by viewModels<MapViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            CityMapperTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background) {
                    DefaultPreview(mapViewModel, fusedLocationClient)
                }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
// @Preview(showBackground = true)
@Composable
fun DefaultPreview(mapViewModel: MapViewModel, fusedLocationClient: FusedLocationProviderClient) {
    CityMapperTheme {


        lateinit var lastLocation: Location

        val markers by mapViewModel.markersLiveData.observeAsState()

        //val singapore = LatLng(1.35, 103.87)

        val userDefaultLocation = LatLng(0.0, 0.0) // Default location

        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        val coroutineScope = rememberCoroutineScope()

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userDefaultLocation, 11f)
        }



        Scaffold() {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                    if (locationPermissionsState.allPermissionsGranted) {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                lastLocation = location
                                val currentLatLng = LatLng(location.latitude, location.longitude)
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(currentLatLng, 11f)
                                    )
                                }
                            }
                        }
                    }
                }
            ) {

            }
        }
    }
}


class utils {
    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
}
