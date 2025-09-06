package com.samoggino.intermit.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.samoggino.intermit.core.DBConfig
import com.samoggino.intermit.data.model.FastingSession

@Database(entities = [FastingSession::class], version = DBConfig.DB_VERSION)
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
                    DBConfig.DB_NAME
                )
                    .fallbackToDestructiveMigration(false) // <-- aggiungi questa riga
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}