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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FormedFormsListViewModel @Inject constructor(
    private val formDAO: FormDAO,
    private val danceDAO: DanceDAO,
    private val dancersDAO: DancersDAO
) : ViewModel() {

    fun getDance(id: Int): Flow<Dance>{
        return danceDAO.getDance(id)
    }
    fun getAllForms(id: Int): Flow<List<Form>> {
        return formDAO.getFormsByDance(id)
    }

    fun getAllDancers(id: Int): Flow<List<Dancer>> {
        return dancersDAO.getAllDancersByForm(id)
    }

    fun addNewForm(form:Form) {
        viewModelScope.launch{
            formDAO.insertForm(form)
        }
    }

    fun removeForm(form:Form) {
        viewModelScope.launch{
            formDAO.deleteForm(form)
        }
    }

    fun removeDancer(dancer: Dancer){
        viewModelScope.launch {
            dancersDAO.deleteDancer(dancer)
        }
    }

    fun clearAllForms(id: Int){
        viewModelScope.launch{
            formDAO.deleteAllForms(id)
        }
    }

    private fun updateForm(form: Form){
        viewModelScope.launch {
            formDAO.updateForm(form)
        }
    }

}