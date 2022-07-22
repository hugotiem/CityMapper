package fr.hugotiem.citymapper.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DetailsComposable(navController: NavController, ) {
    Scaffold {
        AppBar(navController = navController)
        BottomSheetScaffold(
            sheetContent = {

            },
            sheetPeekHeight = 100.dp

        ) {
            GoogleMap()
        }
    }
}

@Composable
fun AppBar(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
        }
        Row() {

        }
    }
}