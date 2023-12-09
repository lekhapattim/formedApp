package hu.ait.formed.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DanceDAO {


    // dance table
    @Query("SELECT * from dancetable")
    fun getAllDances(): Flow<List<Dance>>

    @Query("SELECT * from dancetable WHERE id = :id")
    suspend fun getDance(id: Int): Dance


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDance(item: Dance)

    @Update
    suspend fun updateDance(item: Dance)

    @Delete
    suspend fun deleteDance(item: Dance)

    @Query("DELETE from dancetable")
    suspend fun deleteAllDances()


}