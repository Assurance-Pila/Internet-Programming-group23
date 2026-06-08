package com.netlinq.domain.model

data class QoeFeedback(
    val id: Long = 0,
    val overallRating: Int,
    val responsivenessRating: Int,
    val streamingRating: Int,
    val callQualityRating: Int,
    val satisfactionRating: Int,
    val triggerEvent: String?,
    val networkType: NetworkType?,
    val networkMetricId: Long? = null,
    val metricRecordedAt: Long? = null,
    val signalStrengthSnapshot: Int? = null,
    val latencyMsSnapshot: Int? = null,
    val notes: String?,
    val recordedAt: Long,
    val synced: Boolean = false
)
