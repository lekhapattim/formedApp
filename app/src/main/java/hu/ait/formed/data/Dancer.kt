package hu.ait.formed.data

import android.graphics.Point
import androidx.compose.ui.geometry.Offset
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Dancer(
    var id: Int = 0,
    var offset: Offset,
    var placed: Boolean = false
) : Serializable