package hu.ait.formed.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDAO {


    // formtable

    @Query("SELECT * from formtable WHERE dance_id = :id")
    fun getFormsByDance(id: Int): Flow<List<Form>>

    @Query("SELECT * from formtable WHERE id = :id")
    fun getFormByID(id: Int): Flow<Form>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertForm(item: Form)

    @Update
    suspend fun updateForm(item: Form)

    @Delete
    suspend fun deleteForm(item: Form)

    @Query("DELETE from formtable")
    suspend fun deleteAllDances()





}