package hu.ait.formed.screens.AllDances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.Dance
import hu.ait.formed.data.DanceDAO
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.DancersDAO
import hu.ait.formed.data.Form
import hu.ait.formed.data.FormDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedDanceListViewModel @Inject constructor(
    private val danceDAO: DanceDAO,
    private val formDAO: FormDAO,
    private val dancersDAO: DancersDAO
) : ViewModel() {

    fun getAllDances(): Flow<List<Dance>> {
        return danceDAO.getAllDances()
    }

    fun getAllForms(id: Int): Flow<List<Form>> {
        return formDAO.getFormsByDance(id)
    }

    fun getAllDancers(id: Int): Flow<List<Dancer>> {
        return dancersDAO.getAllDancersByForm(id)
    }

    fun addNewDance(danceItem: Dance) {
        viewModelScope.launch{
            danceDAO.insertDance(danceItem)
        }
    }

    fun removeDancer(dancer: Dancer){
        viewModelScope.launch {
            dancersDAO.deleteDancer(dancer)
        }
    }

    fun removeForm(form: Form){
        viewModelScope.launch {
            formDAO.deleteForm(form)
        }
    }

    fun removeDance(dance: Dance) {
        viewModelScope.launch {
            danceDAO.deleteDance(dance)
        }
    }

}