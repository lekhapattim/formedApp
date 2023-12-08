package hu.ait.formed.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.ait.formed.R
import hu.ait.shoppinglist.R
import java.io.Serializable

@Entity(tableName = "shoppingtable")
data class FormItem(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdate") var createDate: String,
    @ColumnInfo(name = "price") var price: String,
    @ColumnInfo(name = "category") var category: ShoppingCategory,
    @ColumnInfo(name = "status") var status: Boolean
) : Serializable

enum class ShoppingCategory {
    FOOD, ELECTRONICS, TOILETRIES, BOOKS, MISC;

    fun getIcon(): Int {
        return if (this == FOOD) R.drawable.food
        else if (this == ELECTRONICS) R.drawable.electronics
        else if (this == TOILETRIES) R.drawable.toiletries
        else if (this == BOOKS) R.drawable.book
        else R.drawable.misc
    }
}