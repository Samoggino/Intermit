package com.samoggino.intermit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.samoggino.intermit.data.model.FastingSession

@Database(entities = [FastingSession::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fastingDao(): FastingDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "intermit_database"
                )
                    .fallbackToDestructiveMigration(false) // <-- aggiungi questa riga
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}