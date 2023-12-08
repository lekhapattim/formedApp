package hu.ait.formed.data

import android.graphics.Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Dancer(
    var id: Int = 0,
    var point: Point,
    var placed: Boolean = false
) : Serializable