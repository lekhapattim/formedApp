package hu.ait.formed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "formtable",
    foreignKeys = [androidx.room.ForeignKey(
    entity = Dance::class,
    childColumns = ["dance_id"],
    parentColumns = ["id"]
)])
data class Form(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "dance_id") var danceID: Int
) : Serializable