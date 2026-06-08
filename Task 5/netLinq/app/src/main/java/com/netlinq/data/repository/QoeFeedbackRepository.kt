package com.netlinq.data.repository

import com.netlinq.data.local.dao.QoeFeedbackDao
import com.netlinq.data.mapper.toDomain
import com.netlinq.data.mapper.toEntity
import com.netlinq.domain.model.QoeFeedback
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QoeFeedbackRepository @Inject constructor(
    private val dao: QoeFeedbackDao
) {
    fun observeFeedback(): Flow<List<QoeFeedback>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    suspend fun save(feedback: QoeFeedback): Long =
        dao.insert(feedback.toEntity())

    suspend fun getUnsynced(): List<QoeFeedback> =
        dao.getUnsynced().map { it.toDomain() }

    suspend fun markSynced(ids: List<Long>) {
        if (ids.isNotEmpty()) dao.markSynced(ids)
    }

    suspend fun purgeSyncedOlderThan(cutoffMillis: Long) {
        dao.deleteSyncedOlderThan(cutoffMillis)
    }
}
