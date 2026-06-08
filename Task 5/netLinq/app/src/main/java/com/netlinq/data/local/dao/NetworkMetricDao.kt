package com.netlinq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.netlinq.data.local.entity.NetworkMetricEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NetworkMetricDao {

    @Insert
    suspend fun insert(metric: NetworkMetricEntity): Long

    @Query("SELECT * FROM network_metrics ORDER BY recordedAt DESC")
    fun observeAll(): Flow<List<NetworkMetricEntity>>

    @Query("SELECT * FROM network_metrics WHERE synced = 0 ORDER BY recordedAt ASC")
    suspend fun getUnsynced(): List<NetworkMetricEntity>

    @Query("UPDATE network_metrics SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)

    @Query("DELETE FROM network_metrics WHERE synced = 1 AND recordedAt < :cutoff")
    suspend fun deleteSyncedOlderThan(cutoff: Long)
}
