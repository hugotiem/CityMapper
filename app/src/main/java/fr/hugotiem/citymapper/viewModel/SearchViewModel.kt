package fr.hugotiem.citymapper.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SearchViewModel: ViewModel() {


    val lastSearchLiveData: MutableLiveData<List<String>> = MutableLiveData<List<String>>()

    var historical: List<String>
        get() = lastSearchLiveData.value ?: listOf("TRUC", "Autre", "Ouais")
        set(value) {
            lastSearchLiveData.postValue(value)
        }
}