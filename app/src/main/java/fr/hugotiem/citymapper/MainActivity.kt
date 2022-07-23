package fr.hugotiem.citymapper

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocalActivity
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import fr.hugotiem.citymapper.ui.theme.CityMapperTheme
import kotlinx.coroutines.launch
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import fr.hugotiem.citymapper.view.*
import fr.hugotiem.citymapper.viewModel.*

class MainActivity : ComponentActivity() {

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var initRoute: String

    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>()
    private val mapViewModel: MapViewModel by viewModels<MapViewModel>()
    private val searchViewModel: SearchViewModel by viewModels<SearchViewModel>()
    private val resultsViewModel: ResultsViewModel by viewModels<ResultsViewModel>()
    private val detailsViewModel: DetailsViewModel by viewModels<DetailsViewModel>()


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(authViewModel.isConnected()) {
            initRoute = "home"
        } else {
            initRoute = "login"
        }

        Log.d("Main", initRoute)

        setContent {

            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("login") { LoginScreen(navController, authViewModel) }
                composable("home") { CityMapperTheme {
                    // A surface container using the 'background' color from the theme
                    DefaultPreview(mapViewModel, fusedLocationClient, navController)
                } }
                composable("account") { MyAccountScreen(navController) }
                composable("search") { SearchComposable(navController, searchViewModel) }
                composable(
                    "results?query={query}",
                    arguments = listOf(navArgument("userId") { defaultValue = "" })
                ) {
                    ResultsComposable(navController, resultsViewModel)
                }
                composable("details") { DetailsComposable(navController, detailsViewModel) }
            }
        }
    }
}

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
// @Preview(showBackground = true)
@Composable
fun DefaultPreview(
    mapViewModel: MapViewModel,
    fusedLocationClient: FusedLocationProviderClient,
    navController: NavController
) {
    CityMapperTheme {

        lateinit var lastLocation: Location

        val markers by mapViewModel.markersLiveData.observeAsState()

        val userDefaultLocation = LatLng(0.0, 0.0) // Default location

        val locationPermissionsState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )

        val coroutineScope = rememberCoroutineScope()

        val cameraPositionState = rememberCameraPositionState {
            if(locationPermissionsState.allPermissionsGranted) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        lastLocation = location
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        position = CameraPosition.fromLatLngZoom(currentLatLng, 12f)
                    }
                }
            } else {
                position = CameraPosition.fromLatLngZoom(userDefaultLocation, 1f)
            }


        }

        val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Expanded)
        )

        val mapProperties: MutableState<MapProperties> = remember {
            if(locationPermissionsState.allPermissionsGranted) {
                mutableStateOf(MapProperties(isMyLocationEnabled = true))
            } else {
                mutableStateOf(MapProperties())
            }
        }

        BottomSheetScaffold(
            sheetContent = {
                Column(
                    Modifier.fillMaxHeight(.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp)) {
                        SimpleTextField(navController)
                    }
                }
            },
            sheetPeekHeight = 100.dp,
            scaffoldState = bottomSheetScaffoldState,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetBackgroundColor = colorResource(id = R.color.app_green),
        ) {
            Box() {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties.value, // MapProperties(isMyLocationEnabled = true),
                    uiSettings = MapUiSettings(myLocationButtonEnabled = false),
                    onMapLoaded = {
                        locationPermissionsState.launchMultiplePermissionRequest()
                        if (locationPermissionsState.allPermissionsGranted) {
                            mapProperties.value = MapProperties(isMyLocationEnabled = true)
                            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                if (location != null) {
                                    lastLocation = location
                                    val currentLatLng = LatLng(location.latitude, location.longitude)
                                    coroutineScope.launch {
                                        cameraPositionState.animate(
                                            CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                ) {

                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Column(
                        modifier = Modifier.background(color = Color.White)
                    ) {
                        IconButton(onClick = { navController.navigate("account") }) {
                            Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = null)
                        }
                        if(locationPermissionsState.allPermissionsGranted) {
                            IconButton(onClick = {
                                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                                    if (location != null) {
                                        lastLocation = location
                                        val currentLatLng = LatLng(location.latitude, location.longitude)
                                        coroutineScope.launch {
                                            cameraPositionState.animate(
                                                CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f)
                                            )
                                        }
                                    }
                                }
                            }) {
                                Icon(imageVector = Icons.Filled.MyLocation, contentDescription = null)
                            }
                        }
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SimpleTextField(navController: NavController) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("search")
            }
    ) {
        val focusManager = LocalFocusManager.current

        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                modifier = Modifier.padding(end = 20.dp)
            )
            Text("On va où ?")
        }


    }

}