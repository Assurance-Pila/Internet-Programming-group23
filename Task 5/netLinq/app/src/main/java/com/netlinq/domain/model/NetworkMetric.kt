package com.netlinq.domain.model

data class NetworkMetric(
    val id: Long = 0,
    val signalStrength: Int?,
    val signalQuality: Int?,
    val networkType: NetworkType,
    val latencyMs: Int?,
    val deviceModel: String,
    val androidVersion: String,
    val recordedAt: Long,
    val synced: Boolean = false
)
