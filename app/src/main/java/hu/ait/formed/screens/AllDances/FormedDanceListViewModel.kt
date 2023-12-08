package hu.ait.formed.screens.AllDances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Form
import hu.ait.formed.data.FormedDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedDanceListViewModel @Inject constructor(
    private val formedDAO: FormedDAO
) : ViewModel() {

    fun getAllDances(): Flow<List<Dance>> {
        return formedDAO.getAllDances()
    }

    fun addNewDance(danceItem: Dance) {
        viewModelScope.launch{
            formedDAO.insertDance(danceItem)
        }
    }

    fun removeDance(danceItem: Dance) {
        viewModelScope.launch {
            formedDAO.deleteDance(danceItem)
        }
    }

    fun clearAllShopping() {
        viewModelScope.launch {
            formedDAO.deleteAllDances()
        }
    }

}