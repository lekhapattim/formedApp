package hu.ait.formed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Form(
    var id: Int = 0,
    var title: String,
    var dancers: List<Dancer>
) : Serializable