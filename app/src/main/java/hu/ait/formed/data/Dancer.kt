package hu.ait.formed.data

import androidx.compose.ui.geometry.Offset
import java.io.Serializable

data class Dancer(
    var id: Int = 0,
    var offset: Offset,
    var placed: Boolean = false
) : Serializable