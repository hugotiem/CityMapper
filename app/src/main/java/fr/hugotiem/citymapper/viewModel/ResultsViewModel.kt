package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.ViewModel

enum class ScheduleType { START , END }

class ResultsViewModel: ViewModel() {

    fun stringType(type: ScheduleType): String {
        if(type == ScheduleType.START) {
            return "Départ"
        }
        return "Arrivée"
    }
}