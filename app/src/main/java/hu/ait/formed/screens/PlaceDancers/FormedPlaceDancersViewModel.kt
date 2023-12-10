package hu.ait.formed.screens.PlaceDancers

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.Form
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.DancersDAO
import hu.ait.formed.data.FormDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedPlaceDancersViewModel @Inject constructor(
    private val formDAO: FormDAO,
    private val dancersDAO: DancersDAO
) : ViewModel() {

    private var clickedDancer: Int? = null

    var dancerList by mutableStateOf<List<Dancer>>(emptyList())

    fun getForm(id: Int): Flow<Form> {
        return formDAO.getFormByID(id)
    }

    fun updateForm(formItem: Form) {
        viewModelScope.launch {
            formDAO.updateForm(formItem)
        }
    }

    fun insertDancer(dancerItem: Dancer) {
        viewModelScope.launch {
            dancersDAO.insertDancer(dancerItem)
        }
    }

    fun removeDancer(dancerItem: Dancer) {
        viewModelScope.launch {
            dancersDAO.deleteDancer(dancerItem)
        }
    }

    fun setClickedDancer(number: Int?) {
        viewModelScope.launch {
            clickedDancer = number
        }
    }

    fun getClickedDancer(): Int? {
        return clickedDancer
    }

    fun getAllDancersByForm(id: Int): Flow<List<Dancer>> {
        return dancersDAO.getAllDancersByForm(id)
    }

    fun updateDancerPoints(coords: Pair<Offset, Offset>, clickedNum: Int) {
        var dancer =
            dancersDAO.getDancerByID(clickedNum)
    }

}