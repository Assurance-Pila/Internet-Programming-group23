package com.netlinq.monitoring

import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.NetworkType
import com.netlinq.presentation.feedback.FeedbackTriggerEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkDegradationDetector @Inject constructor() {

    fun detect(
        previous: NetworkMetric?,
        current: NetworkMetric,
        triggers: TriggerSettings
    ): FeedbackTriggerEvent? {
        if (previous == null) return null

        if (triggers.connectivityEnabled) {
            val wasOnline = previous.networkType != NetworkType.NONE
            val isOnline = current.networkType != NetworkType.NONE
            if (wasOnline != isOnline) return FeedbackTriggerEvent.CONNECTIVITY_INTERRUPTION
        }

        if (triggers.networkChangeEnabled &&
            previous.networkType != current.networkType &&
            previous.networkType != NetworkType.NONE &&
            current.networkType != NetworkType.NONE
        ) {
            return FeedbackTriggerEvent.NETWORK_TYPE_CHANGE
        }

        if (triggers.signalDropEnabled) {
            val prev = previous.signalStrength
            val curr = current.signalStrength
            if (prev != null && curr != null && curr <= prev - SIGNAL_DROP_DBM) {
                return FeedbackTriggerEvent.SIGNAL_DEGRADATION
            }
        }

        if (triggers.latencySpikeEnabled) {
            val prev = previous.latencyMs
            val curr = current.latencyMs
            if (prev != null && curr != null &&
                curr >= prev + LATENCY_SPIKE_MS &&
                curr >= prev * 2
            ) {
                return FeedbackTriggerEvent.LATENCY_SPIKE
            }
        }

        return null
    }

    data class TriggerSettings(
        val signalDropEnabled: Boolean = true,
        val networkChangeEnabled: Boolean = true,
        val latencySpikeEnabled: Boolean = true,
        val connectivityEnabled: Boolean = true
    )

    companion object {
        private const val SIGNAL_DROP_DBM = 10
        private const val LATENCY_SPIKE_MS = 200
    }
}
