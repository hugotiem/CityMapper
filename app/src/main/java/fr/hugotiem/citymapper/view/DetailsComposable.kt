package fr.hugotiem.citymapper.view

import android.Manifest
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.android.gms.maps.GoogleMapOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Polyline
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.viewModel.DetailsViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class)
@Composable
fun DetailsComposable(navController: NavController, detailsViewModel: DetailsViewModel) {

    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val properties = remember { mutableStateOf(MapProperties()) }

    if(locationPermissionsState.allPermissionsGranted) {
        properties.value = MapProperties(isMyLocationEnabled = true)
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

                    }
                }

            },
            sheetPeekHeight = 180.dp

        ) {
            Box {
                GoogleMap(
                    properties = properties.value,
                    uiSettings = MapUiSettings(zoomControlsEnabled = false),

                ) {
                    Polyline(points = listOf())
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