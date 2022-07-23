package fr.hugotiem.citymapper.view

import android.os.Build
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.model.TransitResult
import fr.hugotiem.citymapper.viewModel.ResultsViewModel
import fr.hugotiem.citymapper.viewModel.ScheduleType
import java.time.Instant
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ResultsComposable(navController: NavController, resultsViewModel: ResultsViewModel) {

    var type: ScheduleType = ScheduleType.START

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
                    ResultsTextField("Départ")
                    Spacer(modifier = Modifier.height(10.dp))
                    ResultsTextField("Arrivée")
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
                LazyColumn(
                    modifier = Modifier.clip(
                        shape = RoundedCornerShape(10.dp)
                    )
                ) {
                    items(listOf<TransitResult>(
                        TransitResult(time = 36, price = 1.9, arrivedAt = Date.from(Instant.now()), steps = listOf("steps"))
                    )) { result ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .padding(10.dp)
                                .clickable {
                                    navController.navigate("details")
                                }
                        ) {
                            Column() {
                                LazyRow() {
                                    items(result.steps) {
                                        // Show steps

                                    }
                                }

                            }
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Row() {
                                    Text(text = "${String.format("%.2f", result.price)}€")
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(text = "${result.time.toString()}min")
                                }
                                val cal: Calendar = Calendar.getInstance()
                                cal.time = result.arrivedAt
                                Text(text = "Arrivée: ${cal.get(Calendar.HOUR_OF_DAY)}:${cal.get(Calendar.MINUTE)}")
                            }
                        }
                        
                        Divider()
                    }
                }
            }

        }
    }
}

@Composable
fun ResultsTextField(schedule: String) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable { }) {
        Row(modifier = Modifier.padding(15.dp)) {
            Text(text = schedule)
            Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
        }
    }
}