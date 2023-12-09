package hu.ait.formed.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DancersDAO {


    // dancers table
    @Query("SELECT * from dancerstable WHERE form_id = :id")
    fun getAllDancersByForm(id: Int): Flow<List<Dancer>>

    @Query("SELECT * from dancerstable WHERE id = :id")
    fun getDancerByID(id: Int): Flow<Dancer>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDancer(item: Dancer)

    @Update
    suspend fun updateDancer(item: Dancer)

    @Delete
    suspend fun deleteDancer(item: Dancer)

    @Query("DELETE from dancerstable")
    suspend fun deleteAllDancers()


}