package hu.ait.formed.screens.animateDance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.formed.data.FormedDAO
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import hu.ait.formed.data.Dance
import hu.ait.formed.data.Dancer
import hu.ait.formed.data.Form
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class AnimateViewModel @Inject constructor(
    private val formedDAO: FormedDAO
) : ViewModel() {

    lateinit var dance: Dance

    var isPlaying by mutableStateOf(false)


    var points by mutableStateOf<List<Offset>>(emptyList())

    suspend fun getDance(id: Int): Dance {
        return formedDAO.getDance(id)
    }

    fun startAnimation() {
        isPlaying = true
        viewModelScope.launch {
            if(isPlaying) {
                dance.forms.forEach { form: Form ->
                    points = formToPoints(form)
                    delay(2000)
                    // TODO: create animated movements
                }
                isPlaying = false
            }
        }
    }


    fun formToPoints(form: Form): List<Offset>  {
        val list = emptyList<Offset>().toMutableList()
        form.dancers.forEach {dancer: Dancer ->
            list += dancer.offset
        }
        return list
    }


}