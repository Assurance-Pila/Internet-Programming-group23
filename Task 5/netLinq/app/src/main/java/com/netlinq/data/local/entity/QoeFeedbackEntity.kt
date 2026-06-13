package com.netlinq.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "qoe_feedback",
    foreignKeys = [
        ForeignKey(
            entity = NetworkMetricEntity::class,
            parentColumns = ["id"],
            childColumns = ["networkMetricId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("networkMetricId")]
)
data class QoeFeedbackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val overallRating: Int,
    val responsivenessRating: Int,
    val streamingRating: Int,
    val callQualityRating: Int,
    val satisfactionRating: Int,
    val triggerEvent: String?,
    val networkType: String?,
    val networkMetricId: Long? = null,
    val metricRecordedAt: Long? = null,
    val signalStrengthSnapshot: Int? = null,
    val latencyMsSnapshot: Int? = null,
    val notes: String?,
    val recordedAt: Long,
    val synced: Boolean = false
)
