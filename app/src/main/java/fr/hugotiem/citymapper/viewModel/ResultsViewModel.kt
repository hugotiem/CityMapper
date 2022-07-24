package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.ViewModel
import fr.hugotiem.citymapper.providers.SearchProvider
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

enum class ScheduleType { START , END }

class ResultsViewModel: ViewModel() {

    fun stringType(type: ScheduleType): String {
        if(type == ScheduleType.START) {
            return "Départ"
        }
        return "Arrivée"
    }

    suspend fun getResults(): Unit = coroutineScope {
        launch {
            val res = SearchProvider.service.getResults()
            if(res.isSuccessful) {
                val resuluts = res.body()
            } else {

            }
        }
    }
}