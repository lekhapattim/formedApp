package hu.ait.formed.data

import android.graphics.Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dancetable")
data class Dance(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "numDancers") var numDancers: Int

) : Serializable

