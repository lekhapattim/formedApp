package hu.ait.formed.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Dance::class, Form::class, Dancer::class], version = 1, exportSchema = false)
abstract class FormedDatabase : RoomDatabase() {

    abstract fun danceDAO(): DanceDAO
    abstract fun formDAO(): FormDAO
    abstract fun dancersDAO(): DancersDAO

    companion object {
        @Volatile
        private var Instance: FormedDatabase? = null

        fun getDatabase(context: Context): FormedDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FormedDatabase::class.java,
                    "formed_database.db")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}