package hu.ait.formed.screens.AllDances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.DanceDAO
import hu.ait.formed.data.Form
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedDanceListViewModel @Inject constructor(
    private val danceDAO: DanceDAO
) : ViewModel() {

    fun getAllDances(): Flow<List<Dance>> {
        return danceDAO.getAllDances()
    }

    fun addNewDance(danceItem: Dance) {
        viewModelScope.launch{
            danceDAO.insertDance(danceItem)
        }
    }

    fun removeDance(danceItem: Dance) {
        viewModelScope.launch {
            danceDAO.deleteDance(danceItem)
        }
    }

    fun clearAllDances() {
        viewModelScope.launch {
            danceDAO.deleteAllDances()
        }
    }

}