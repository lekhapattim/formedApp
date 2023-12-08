package hu.ait.formed.data

import android.graphics.Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dancerstable")
data class Dancer(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "point") var point: Point,
    @ColumnInfo(name = "placed") var placed: Boolean = false
) : Serializable