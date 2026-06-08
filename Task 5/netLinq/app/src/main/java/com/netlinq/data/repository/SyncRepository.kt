package com.netlinq.data.repository

import com.netlinq.BuildConfig
import com.netlinq.data.remote.NetworkMetricPayload
import com.netlinq.data.remote.QoeFeedbackPayload
import com.netlinq.data.remote.SupabaseApi
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    private val supabaseApi: SupabaseApi,
    private val deviceRepository: DeviceRepository,
    private val networkMetricRepository: NetworkMetricRepository,
    private val qoeFeedbackRepository: QoeFeedbackRepository
) {
    private val isoFormatter = DateTimeFormatter.ISO_INSTANT

    fun isConfigured(): Boolean =
        BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_ANON_KEY.isNotBlank()

    suspend fun syncAll(): Result<Int> {
        if (!isConfigured()) return Result.failure(IllegalStateException("Supabase not configured"))

        val deviceHash = deviceRepository.getDeviceHash()
        var uploaded = 0

        val metrics = networkMetricRepository.getUnsynced()
        if (metrics.isNotEmpty()) {
            val payload = metrics.map { metric ->
                NetworkMetricPayload(
                    device_hash = deviceHash,
                    signal_strength = metric.signalStrength,
                    signal_quality = metric.signalQuality,
                    network_type = metric.networkType.label,
                    latency_ms = metric.latencyMs,
                    device_model = metric.deviceModel,
                    android_version = metric.androidVersion,
                    recorded_at = formatTimestamp(metric.recordedAt)
                )
            }
            val response = supabaseApi.uploadNetworkMetrics(payload)
            if (!response.isSuccessful) {
                return Result.failure(Exception("Metrics sync failed: ${response.code()}"))
            }
            networkMetricRepository.markSynced(metrics.map { it.id })
            uploaded += metrics.size
        }

        val feedback = qoeFeedbackRepository.getUnsynced()
        if (feedback.isNotEmpty()) {
            val payload = feedback.map { item ->
                QoeFeedbackPayload(
                    device_hash = deviceHash,
                    overall_rating = item.overallRating,
                    responsiveness_rating = item.responsivenessRating,
                    streaming_rating = item.streamingRating,
                    call_quality_rating = item.callQualityRating,
                    satisfaction_rating = item.satisfactionRating,
                    trigger_event = item.triggerEvent,
                    network_type = item.networkType?.label,
                    network_metric_id = item.networkMetricId,
                    metric_recorded_at = item.metricRecordedAt?.let(::formatTimestamp),
                    signal_strength_snapshot = item.signalStrengthSnapshot,
                    latency_ms_snapshot = item.latencyMsSnapshot,
                    notes = item.notes,
                    recorded_at = formatTimestamp(item.recordedAt)
                )
            }
            val response = supabaseApi.uploadQoeFeedback(payload)
            if (!response.isSuccessful) {
                return Result.failure(Exception("Feedback sync failed: ${response.code()}"))
            }
            qoeFeedbackRepository.markSynced(feedback.map { it.id })
            uploaded += feedback.size
        }

        return Result.success(uploaded)
    }

    suspend fun purgeOldRecords(retentionDays: Int = 7) {
        val cutoff = System.currentTimeMillis() - retentionDays * 24L * 60L * 60L * 1000L
        networkMetricRepository.purgeSyncedOlderThan(cutoff)
        qoeFeedbackRepository.purgeSyncedOlderThan(cutoff)
    }

    private fun formatTimestamp(epochMillis: Long): String =
        isoFormatter.format(Instant.ofEpochMilli(epochMillis).atOffset(ZoneOffset.UTC))
}
