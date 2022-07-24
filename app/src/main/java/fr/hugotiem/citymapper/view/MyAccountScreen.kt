package fr.hugotiem.citymapper.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.hugotiem.citymapper.R
import fr.hugotiem.citymapper.viewModel.MyAccountViewModel


@Composable
fun MyAccountScreen(navController: NavController) {

    val myAccountViewModel: MyAccountViewModel = MyAccountViewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                contentColor = Color.White,
                backgroundColor = colorResource(id = R.color.app_green),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                title = {
                    Text("Mon compte")
                }
            )
        }
    ) {

        Column {

            myAccountItem(
                child = {
                    Text(text = myAccountViewModel.getUsername() ?: "noname")
                }
            )

            // STATS
            myAccountItem(
                child = {
                    Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround) {
                        StatItem(image = 1, data = "7785", desc = "Calories", color = colorResource(
                            id = R.color.teal_700))
                        StatItem(image = 2, data = "262,5kg", desc = "CO2 économisé", color = colorResource(
                            id = R.color.app_green))
                        StatItem(image = 3, data = "2765€", desc = "De", color = colorResource(id = R.color.purple_500))
                    }
                }
            )

            // LOGOUT
            myAccountItem(
                onClick = {
                    myAccountViewModel.logout()
                },
                child = {
                    Text(text = "Se déconnecter", color = Color.Red);
                }
            )
        }

    }
}

@Composable
fun myAccountItem(child: @Composable () -> Unit, onClick: (() -> Unit)? = null) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick ?: {})
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box(modifier = Modifier.padding(20.dp)) {
            child()
        }
    }
}

@Composable
fun StatItem(image: Int, data: String, desc: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = Icons.Filled.AccountTree, contentDescription = null, tint = color)
        Text(text = data, color = color)
        Text(text = desc, color = color)
    }
}

