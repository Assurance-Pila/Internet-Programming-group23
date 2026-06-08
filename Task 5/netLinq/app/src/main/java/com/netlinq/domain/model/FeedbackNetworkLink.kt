package com.netlinq.domain.model

data class FeedbackNetworkLink(
    val metricId: Long? = null,
    val networkType: NetworkType? = null,
    val signalStrength: Int? = null,
    val latencyMs: Int? = null,
    val metricRecordedAt: Long? = null
) {
    companion object {
        fun fromMetric(metric: NetworkMetric?): FeedbackNetworkLink? {
            metric ?: return null
            return FeedbackNetworkLink(
                metricId = metric.id.takeIf { it > 0 },
                networkType = metric.networkType,
                signalStrength = metric.signalStrength,
                latencyMs = metric.latencyMs,
                metricRecordedAt = metric.recordedAt
            )
        }
    }
}
