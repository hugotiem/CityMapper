package fr.hugotiem.citymapper.view

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.model.*
import fr.hugotiem.citymapper.viewModel.ResultsViewModel
import fr.hugotiem.citymapper.viewModel.ScheduleType
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResultsComposable(navController: NavController, resultsViewModel: ResultsViewModel) {

    val resultsState = resultsViewModel.resultsLiveData.observeAsState()
    val selectedState: MutableState<Leg?> = remember {
        mutableStateOf(null)
    }

    var type: ScheduleType = ScheduleType.START

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        resultsViewModel.searchItineraries()
    }

    if(selectedState.value != null) {
        DetailsComposable(navController = navController, selected = selectedState.value!!, resultsViewModel)
    } else {
        Scaffold(
            backgroundColor = colorResource(id = R.color.app_green)
        ) {
            Column() {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        IconButton(
                            onClick = { /*Launch refresh query*/ },
                            modifier = Modifier
                                .clip(
                                    shape = CircleShape,
                                )
                                .background(color = Color.White)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(
                                    shape = CircleShape,
                                )
                                .background(color = Color.White)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(15.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Timer, contentDescription = null)
                                //Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "${resultsViewModel.stringType(type)} :  Maintenant",
                                    modifier = Modifier
                                        .background(color = Color.White)
                                )
                            }
                        }
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .padding(vertical = 20.dp)
                ) {
                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(end = 10.dp)) {
                        ResultsTextField("Départ", "Localisation acutelle")
                        Spacer(modifier = Modifier.height(10.dp))
                        ResultsTextField("Arrivée", resultsViewModel.destinationName)
                    }
                    Box () {
                        IconButton(onClick = {  }) {
                            Icon(imageVector = Icons.Filled.SwapVert, contentDescription = null, tint = Color.White)
                        }
                    }
                }
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Suggérés",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 20.dp)
                    )

                    //var res = ApiResult(bounds = listOf(LatLng(48.797035, 2.458125)), legs = listOf())

                    if(resultsState.value != null) {
                        LazyColumn(
                            modifier = Modifier.clip(
                                shape = RoundedCornerShape(10.dp)
                            )
                        ) {
                            items(resultsState.value!!.legs) { result ->
                                TransitResultItem(navController = navController, result = result, selectedState = selectedState)
                                Divider()
                            }
                        }
                    }
                }

            }
        }
    }

}

@Composable
fun ResultsTextField(schedule: String, text: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { }) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = schedule)
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
            Spacer(modifier = Modifier.width(20.dp))
            Text(text = text)
        }
    }
}


@Composable
fun TransitResultItem(navController: NavController, result: Leg, selectedState: MutableState<Leg?>?) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(10.dp)
            .clickable {
                Log.d("MESS", "ON CLICK")
                selectedState?.value = result
            }
    ) {

        Column() {
            if(result.travelMode == TravelMode.transit) {
                LazyRow(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(result.steps) { step ->
                        // Show steps
                        if(step is TransitStep) {
                            val detail = step.transitDetail
                            Box(modifier = Modifier
                                .padding(4.dp)
                                .size(40.dp)
                                .aspectRatio(1f)
                                .background(Color(android.graphics.Color.parseColor(detail.color)),
                                    shape = CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = detail.name, color = Color.White,   textAlign = TextAlign.Center)
                            }
                        } else {
                            Icon(imageVector = Icons.Filled.DirectionsWalk, contentDescription = null)
                        }
                    }
                }
            } else {
                // display walking icon
                Icon(imageVector = Icons.Filled.People, contentDescription = null)
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row() {
                Text(text = "${String.format("%.2f", result.price)}€")
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "${result.duration.toString()}min")
            }
            //val cal: Calendar = Calendar.getInstance()
            //cal.time = result.arrivedAt
            Text(text = "Arrivée: 12:05")//${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}")
        }
    }


}
