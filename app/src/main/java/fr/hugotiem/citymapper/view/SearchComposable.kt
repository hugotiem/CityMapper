package fr.hugotiem.citymapper.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.viewModel.SearchViewModel
import kotlinx.coroutines.job


@Composable
fun SearchComposable(navController: NavController, searchViewModel: SearchViewModel) {

    val recentsState = searchViewModel.lastSearchLiveData.observeAsState()
    val recents = searchViewModel.historical
    val text: MutableState<String> = remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val focusRequester = FocusRequester()


    Scaffold(
        backgroundColor = colorResource(id = R.color.app_green)
    ) {

        Column() {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(20.dp)
                    )
                }
                Card(
                    modifier = Modifier.padding(top = 20.dp, bottom = 20.dp, end = 20.dp),
                    backgroundColor = Color.White,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester = focusRequester),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White
                        ),
                        value = text.value,
                        onValueChange = { newText ->
                            text.value = newText
                        },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                /* SEARCH ITNIERARY */
                                focusManager.clearFocus()
                            }
                        ),
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        },
                        placeholder = {
                            Text("On va où ?")
                        },
                    )
                }
            }

            if(text.value.isNotEmpty()) {
                Text(
                    text = "Résultats",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(20.dp)
                )
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.White)
                        .fillMaxWidth(),

                    ) {
                    LazyColumn(
                    ) {
                        items(recents) { search ->
                            Column() {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth()
                                        .clickable { },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
                                    Text(text = search, modifier = Modifier
                                        .padding(start = 5.dp)
                                        .fillMaxWidth())
                                }
                                if(recents.indexOf(search) != recents.size - 1) {
                                    Divider()
                                }
                            }
                        }
                    }
                }

            }

                Text(
                    text = "Récents",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(20.dp)
                )
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color.White)
                        .fillMaxWidth(),

                ) {
                    LazyColumn(
                    ) {
                        items(recents) { search ->
                            Column(
                                modifier = Modifier.clickable {
                                    navController.navigate("results?query=$search")
                                }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(imageVector = Icons.Filled.LocationOn, contentDescription = null)
                                    Text(text = search, modifier = Modifier.padding(start = 5.dp))
                                }
                                if(recents.indexOf(search) != recents.size - 1) {
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
    }
    LaunchedEffect(Unit) {
        this.coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }

}