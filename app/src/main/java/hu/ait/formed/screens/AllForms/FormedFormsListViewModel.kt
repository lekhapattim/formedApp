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
class FormedFormsListViewModel @Inject constructor(
    private val formedDAO: FormedDAO
) : ViewModel() {

    lateinit var dance: Dance

    suspend fun getDance(id: Int): Dance {
        return formedDAO.getDance(id)
    }


    fun getAllForms(dance: Dance): MutableList<Form> {
        return dance.forms
    }

    fun addNewForm(dance:Dance, form:Form) {
        dance.forms.add(form)
        updateDance(dance)

    }

    fun removeForm(dance:Dance, form:Form) {
        dance.forms.remove(form)
        updateDance(dance)

    }

    fun clearAllForms(dance: Dance){
        dance.forms.clear()
        updateDance(dance)
    }

    private fun updateDance(dance: Dance){
        viewModelScope.launch {
            formedDAO.updateDance(dance)
        }
    }

}