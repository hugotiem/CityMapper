package fr.hugotiem.citymapper.view

import android.Manifest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.model.Leg
import fr.hugotiem.citymapper.model.TransitStep
import fr.hugotiem.citymapper.viewModel.DetailsViewModel
import fr.hugotiem.citymapper.viewModel.ResultsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun DetailsComposable(navController: NavController, selected: Leg, viewModel: ResultsViewModel? = null) {

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val properties = remember { mutableStateOf(MapProperties()) }

    val coroutineScope = rememberCoroutineScope()

    if(locationPermissionsState.allPermissionsGranted) {
        properties.value = MapProperties(isMyLocationEnabled = true)
    }

    val cameraPositionState = rememberCameraPositionState {
        if (locationPermissionsState.allPermissionsGranted) {


            position = CameraPosition.fromLatLngZoom( viewModel!!.startLatLng, 10f)
        }
    }

    LaunchedEffect(Unit) {
        val start = viewModel?.startLatLng
        val end = viewModel?.endLatLng

        var minx: Double = 0.0
        var miny: Double = 0.0
        var maxx: Double = 0.0
        var maxy: Double = 0.0

        if(start!!.latitude <= end!!.latitude) {
            miny = start.latitude
        } else {
            miny = end.latitude
        }
        if(start.longitude <= end.longitude) {
            minx = start.longitude
        } else {
            minx = end.longitude
        }

        if(start.latitude <= end.latitude) {
            maxy = end.latitude
        } else {
            maxy = start.latitude
        }

        if(start.longitude <= end.longitude) {
            maxx = end.longitude
        } else {
            maxx = start.longitude
        }

        val bound = LatLngBounds(
            LatLng(miny, minx),
            LatLng(maxy, maxx)
        )
        coroutineScope.launch {
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngBounds(bound,
                    50)
            )
        }
    }



    Scaffold {
        AppBar(navController = navController)
        BottomSheetScaffold(
            sheetBackgroundColor = Color.Transparent,
            sheetElevation = 0.dp,
            sheetContent = {
                Box(modifier =  Modifier.align(alignment = Alignment.End)) {
                    Button(
                        modifier = Modifier.padding(10.dp),
                        onClick = { /*TODO*/ },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.app_green))
                    ) {
                        Text(
                            text = "J'y vais !",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                Box() {
                    Column(
                        Modifier
                            .fillMaxHeight(.7f)
                            .fillMaxWidth()
                            .background(color = Color.White),
                    ) {
                        //TransitResultItem(navController = navController, result = result)
                        TransitResultItem(navController, result = selected, null)
                    }
                }

            },
            sheetPeekHeight = 180.dp

        ) {
            Box {
                GoogleMap(
                    properties = properties.value,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false),
                    cameraPositionState = cameraPositionState

                ) {
                    for (polyline in selected.steps) {
                        val color: Color
                        if(polyline is TransitStep) {
                            val details = polyline.transitDetail
                            color = Color(android.graphics.Color.parseColor(details.color))
                        } else {
                            color = Color.Gray
                        }
                        Log.d("POLY", polyline.polyline.toString())
                        Polyline(points = polyline.polyline, color = color)
                    }

                }
                AppBar(navController = navController)
            }
        }
    }
}

@Composable
fun AppBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }, modifier = Modifier
            .clip(shape = CircleShape)
            .background(color = Color.White)) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Row() {

        }
    }
}