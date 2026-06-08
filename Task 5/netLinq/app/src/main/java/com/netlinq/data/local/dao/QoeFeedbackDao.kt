package com.netlinq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.netlinq.data.local.entity.QoeFeedbackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QoeFeedbackDao {

    @Insert
    suspend fun insert(feedback: QoeFeedbackEntity): Long

    @Query("SELECT * FROM qoe_feedback ORDER BY recordedAt DESC")
    fun observeAll(): Flow<List<QoeFeedbackEntity>>

    @Query("SELECT * FROM qoe_feedback WHERE synced = 0 ORDER BY recordedAt ASC")
    suspend fun getUnsynced(): List<QoeFeedbackEntity>

    @Query("UPDATE qoe_feedback SET synced = 1 WHERE id IN (:ids)")
    suspend fun markSynced(ids: List<Long>)

    @Query("DELETE FROM qoe_feedback WHERE synced = 1 AND recordedAt < :cutoff")
    suspend fun deleteSyncedOlderThan(cutoff: Long)
}
