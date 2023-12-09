package hu.ait.formed.data

import android.graphics.Point
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "dancerstable",
    foreignKeys = [ForeignKey(
        entity = Form::class,
        childColumns = ["form_id"],
        parentColumns = ["id"]
    )])
data class Dancer(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "point") var point: Point,
    @ColumnInfo(name = "placed") var placed: Boolean = false,
    @ColumnInfo(name = "form_id") var formID: Int
) : Serializable