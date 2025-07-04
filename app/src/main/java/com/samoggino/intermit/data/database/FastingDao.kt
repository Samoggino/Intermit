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
    suspend fun insertSession(session: FastingSession): Long

    @Update
    suspend fun updateSession(session: FastingSession)

    @Delete
    suspend fun deleteSession(session: FastingSession)

    @Query("DELETE FROM fasting_sessions")
    suspend fun deleteAllSessions()

    @Query("SELECT * FROM fasting_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<FastingSession>>

    @Query("SELECT * FROM fasting_sessions WHERE status != 'COMPLETED' AND status != 'STOPPED' ORDER BY startTime DESC LIMIT 1")
    suspend fun getCurrentNonTerminatedSession(): FastingSession?

    @Query("SELECT * FROM fasting_sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): FastingSession?

}
