package com.netlinq.data.repository

import com.netlinq.data.local.dao.NetworkMetricDao
import com.netlinq.data.mapper.toDomain
import com.netlinq.data.mapper.toEntity
import com.netlinq.domain.model.NetworkMetric
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMetricRepository @Inject constructor(
    private val dao: NetworkMetricDao
) {
    fun observeMetrics(): Flow<List<NetworkMetric>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    suspend fun save(metric: NetworkMetric): Long =
        dao.insert(metric.toEntity())

    suspend fun getUnsynced(): List<NetworkMetric> =
        dao.getUnsynced().map { it.toDomain() }

    suspend fun markSynced(ids: List<Long>) {
        if (ids.isNotEmpty()) dao.markSynced(ids)
    }

    suspend fun purgeSyncedOlderThan(cutoffMillis: Long) {
        dao.deleteSyncedOlderThan(cutoffMillis)
    }
}
