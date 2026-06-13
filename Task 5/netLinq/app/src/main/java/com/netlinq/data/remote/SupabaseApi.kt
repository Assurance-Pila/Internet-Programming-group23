package com.netlinq.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface SupabaseApi {

    @POST("rest/v1/network_metrics")
    @Headers("Prefer: return=minimal")
    suspend fun uploadNetworkMetrics(
        @Body metrics: List<NetworkMetricPayload>
    ): Response<Unit>

    @POST("rest/v1/qoe_feedback")
    @Headers("Prefer: return=minimal")
    suspend fun uploadQoeFeedback(
        @Body feedback: List<QoeFeedbackPayload>
    ): Response<Unit>
}

data class NetworkMetricPayload(
    val device_hash: String,
    val client_metric_id: Long,
    val signal_strength: Int?,
    val signal_quality: Int?,
    val network_type: String,
    val latency_ms: Int?,
    val device_model: String,
    val android_version: String,
    val recorded_at: String
)

data class QoeFeedbackPayload(
    val device_hash: String,
    val overall_rating: Int,
    val responsiveness_rating: Int,
    val streaming_rating: Int,
    val call_quality_rating: Int,
    val satisfaction_rating: Int,
    val trigger_event: String?,
    val network_type: String?,
    val network_metric_id: Long? = null,
    val metric_recorded_at: String? = null,
    val signal_strength_snapshot: Int? = null,
    val latency_ms_snapshot: Int? = null,
    val notes: String?,
    val recorded_at: String
)
