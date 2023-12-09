package hu.ait.formed.data

import androidx.compose.ui.geometry.Offset
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
    @ColumnInfo(name = "x") var x: Float,
    @ColumnInfo(name = "y") var y: Float,
    @ColumnInfo(name = "placed") var placed: Boolean = false,
    @ColumnInfo(name = "form_id") var formID: Int
) : Serializable