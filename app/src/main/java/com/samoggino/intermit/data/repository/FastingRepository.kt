package com.samoggino.intermit.data.repository

import com.samoggino.intermit.data.database.FastingDao
import com.samoggino.intermit.data.model.FastingSession
import kotlinx.coroutines.flow.Flow

class FastingRepository(private val dao: FastingDao) {

    val allSessions: Flow<List<FastingSession>> = dao.getAllSessions()

    suspend fun insert(session: FastingSession) : Long = dao.insertSession(session)

    suspend fun update(session: FastingSession) = dao.updateSession(session)

    suspend fun delete(session: FastingSession) = dao.deleteSession(session)

    suspend fun deleteAll() = dao.deleteAllSessions()

    suspend fun getCurrentNonTerminatedSession(): FastingSession? {
        return dao.getCurrentNonTerminatedSession()
    }

    suspend fun getSessionById(id: Long): FastingSession? {
        return dao.getSessionById(id)
    }

}
