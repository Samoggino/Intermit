package com.samoggino.intermit.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.samoggino.intermit.data.model.FastingSession
import kotlinx.coroutines.flow.Flow

@Dao
interface FastingDao {

    @Insert
    suspend fun insertSession(session: FastingSession) : Long

    @Update
    suspend fun updateSession(session: FastingSession)

    @Delete
    suspend fun deleteSession(session: FastingSession)

    @Query("SELECT * FROM fasting_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<FastingSession>>
}
