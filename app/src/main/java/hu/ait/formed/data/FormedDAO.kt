package hu.ait.formed.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FormedDAO {
    @Query("SELECT * from shoppingtable")
    fun getAllItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingtable WHERE id = :id")
    fun getItem(id: Int): Flow<ShoppingItem>

    @Query("SELECT * from shoppingtable ORDER BY price ASC")
    fun priceAsc(): Flow<List<ShoppingItem>>

    @Query("SELECT * from shoppingtable ORDER BY price DESC")
    fun priceDesc(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ShoppingItem)

    @Update
    suspend fun update(item: ShoppingItem)

    @Delete
    suspend fun delete(item: ShoppingItem)

    @Query("DELETE from shoppingtable")
    suspend fun deleteAllItems()
}