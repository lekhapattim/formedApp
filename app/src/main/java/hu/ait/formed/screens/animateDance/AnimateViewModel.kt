package hu.ait.formed.screens.animateDance

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import hu.ait.formed.data.Dance
import hu.ait.formed.data.DanceDAO
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.DancersDAO
import hu.ait.formed.data.Form
import hu.ait.formed.data.FormDAO
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class AnimateViewModel @Inject constructor(
    private val danceDAO: DanceDAO,
    private val formDAO: FormDAO,
    private val dancersDAO: DancersDAO
) : ViewModel() {

    var isPlaying by mutableStateOf(false)

    var dancerList by mutableStateOf<List<Dancer>>(emptyList())

    fun getDance(id: Int): Flow<Dance> {
        return danceDAO.getDance(id)
    }

    fun getFormsByDance(id: Int): Flow<List<Form>> {
        return formDAO.getFormsByDance(id)
    }
    
    fun getAllDancersByForm(id: Int): Flow<List<Dancer>> {
        return dancersDAO.getAllDancersByForm(id)
    }


}