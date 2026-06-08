package com.netlinq.monitoring

import android.os.Build
import com.netlinq.data.repository.NetworkMetricRepository
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.NetworkType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitorService @Inject constructor(
    private val networkTypeDetector: NetworkTypeDetector,
    private val signalStrengthCollector: SignalStrengthCollector,
    private val latencyMeasurer: LatencyMeasurer,
    private val networkMetricRepository: NetworkMetricRepository
) {
    suspend fun collectAndSave(): NetworkMetric {
        val networkType = networkTypeDetector.detect()
        val signal = signalStrengthCollector.collect()
        val latency = if (networkType != NetworkType.NONE) {
            latencyMeasurer.measure()
        } else {
            null
        }

        val metric = NetworkMetric(
            signalStrength = signal.strengthDbm,
            signalQuality = signal.quality,
            networkType = networkType,
            latencyMs = latency,
            deviceModel = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            recordedAt = System.currentTimeMillis()
        )

        val id = networkMetricRepository.save(metric)
        return metric.copy(id = id)
    }
}
