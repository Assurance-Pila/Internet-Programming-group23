package com.netlinq.monitoring

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.netlinq.data.preferences.AppPreferences
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.NetworkType
import com.netlinq.presentation.feedback.FeedbackTriggerEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

data class NetworkPromptEvent(
    val trigger: FeedbackTriggerEvent,
    val networkType: String,
    val detail: String? = null,
    val metricId: Long? = null,
    val metricRecordedAt: Long? = null,
    val signalStrength: Int? = null,
    val latencyMs: Int? = null
)

@Singleton
class NetworkMonitoringManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val networkMonitorService: NetworkMonitorService,
    private val networkTypeDetector: NetworkTypeDetector,
    private val degradationDetector: NetworkDegradationDetector,
    private val appPreferences: AppPreferences
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _promptEvents = MutableSharedFlow<NetworkPromptEvent>(extraBufferCapacity = 1)
    val promptEvents: SharedFlow<NetworkPromptEvent> = _promptEvents.asSharedFlow()

    private var lastMetric: NetworkMetric? = null
    private var lastPromptAt: Long = 0L
    private var monitoringJobActive = false
    private var wasOnline = true

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            scope.launch { handleConnectivityLost() }
        }

        override fun onAvailable(network: Network) {
            scope.launch {
                handleConnectivityRestored()
                sampleAndDetect()
            }
        }

        override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
            scope.launch { sampleAndDetect() }
        }
    }

    fun start() {
        if (monitoringJobActive) return
        monitoringJobActive = true

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)

        scope.launch {
            while (isActive) {
                val enabled = appPreferences.monitoringEnabled.first()
                if (enabled) {
                    sampleAndDetect()
                }
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private suspend fun handleConnectivityLost() {
        val enabled = appPreferences.monitoringEnabled.first()
        if (!enabled || !wasOnline) return
        val triggers = appPreferences.getTriggerSettings()
        if (!triggers.connectivityEnabled) return

        wasOnline = false
        val metric = lastMetric ?: return
        emitIfNeeded(
            FeedbackTriggerEvent.CONNECTIVITY_INTERRUPTION,
            metric.copy(networkType = NetworkType.NONE),
            detail = "Connection lost"
        )
    }

    private suspend fun handleConnectivityRestored() {
        if (wasOnline) return
        val enabled = appPreferences.monitoringEnabled.first()
        if (!enabled) return
        val triggers = appPreferences.getTriggerSettings()
        if (!triggers.connectivityEnabled) return

        wasOnline = true
        val type = networkTypeDetector.detect()
        val metric = lastMetric ?: return
        emitIfNeeded(
            FeedbackTriggerEvent.CONNECTIVITY_INTERRUPTION,
            metric.copy(networkType = type),
            detail = "Connection restored, now on ${type.label}"
        )
    }

    private suspend fun sampleAndDetect() {
        val enabled = appPreferences.monitoringEnabled.first()
        if (!enabled) return

        val metric = try {
            networkMonitorService.collectAndSave()
        } catch (_: Exception) {
            return
        }

        val triggers = appPreferences.getTriggerSettings()
        val event = degradationDetector.detect(lastMetric, metric, triggers)
        if (event != null) {
            val detail = buildDetail(lastMetric, metric, event)
            emitIfNeeded(event, metric, detail)
        }
        lastMetric = metric
    }

    private suspend fun emitIfNeeded(
        trigger: FeedbackTriggerEvent,
        metric: NetworkMetric,
        detail: String? = null
    ) {
        if (!canPromptNow()) return
        lastPromptAt = System.currentTimeMillis()
        _promptEvents.emit(
            NetworkPromptEvent(
                trigger = trigger,
                networkType = metric.networkType.label,
                detail = detail,
                metricId = metric.id.takeIf { it > 0 },
                metricRecordedAt = metric.recordedAt,
                signalStrength = metric.signalStrength,
                latencyMs = metric.latencyMs
            )
        )
    }

    private suspend fun canPromptNow(): Boolean {
        val frequency = appPreferences.feedbackFrequency.first()
        val cooldown = when (frequency) {
            AppPreferences.FEEDBACK_FREQUENCY_LOW -> 30 * 60 * 1000L
            AppPreferences.FEEDBACK_FREQUENCY_HIGH -> 5 * 60 * 1000L
            else -> 15 * 60 * 1000L
        }
        return System.currentTimeMillis() - lastPromptAt >= cooldown
    }

    private fun buildDetail(
        previous: NetworkMetric?,
        current: NetworkMetric,
        event: FeedbackTriggerEvent
    ): String? = when (event) {
        FeedbackTriggerEvent.NETWORK_TYPE_CHANGE -> {
            val from = previous?.networkType?.label ?: "?"
            "${from} → ${current.networkType.label}"
        }
        FeedbackTriggerEvent.SIGNAL_DEGRADATION -> {
            val from = previous?.signalStrength
            val to = current.signalStrength
            if (from != null && to != null) {
                "${signalStrengthLabel(from)} to ${signalStrengthLabel(to)}"
            } else null
        }
        FeedbackTriggerEvent.LATENCY_SPIKE -> {
            val from = previous?.latencyMs
            val to = current.latencyMs
            if (from != null && to != null) "Was ${from} ms, now ${to} ms" else null
        }
        else -> null
    }

    private fun signalStrengthLabel(dbm: Int): String = when {
        dbm >= -70 -> "strong signal"
        dbm >= -85 -> "OK signal"
        else -> "weak signal"
    }

    companion object {
        private const val POLL_INTERVAL_MS = 60_000L
    }
}
