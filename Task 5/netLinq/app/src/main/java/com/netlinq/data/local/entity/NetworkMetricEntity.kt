package com.netlinq.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "network_metrics")
data class NetworkMetricEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val signalStrength: Int?,
    val signalQuality: Int?,
    val networkType: String,
    val latencyMs: Int?,
    val deviceModel: String,
    val androidVersion: String,
    val recordedAt: Long,
    val synced: Boolean = false
)
