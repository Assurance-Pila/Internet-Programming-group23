package com.netlinq.data.mapper

import com.netlinq.data.local.entity.NetworkMetricEntity
import com.netlinq.data.local.entity.QoeFeedbackEntity
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.NetworkType
import com.netlinq.domain.model.QoeFeedback

fun NetworkMetricEntity.toDomain(): NetworkMetric = NetworkMetric(
    id = id,
    signalStrength = signalStrength,
    signalQuality = signalQuality,
    networkType = NetworkType.fromLabel(networkType),
    latencyMs = latencyMs,
    deviceModel = deviceModel,
    androidVersion = androidVersion,
    recordedAt = recordedAt,
    synced = synced
)

fun NetworkMetric.toEntity(): NetworkMetricEntity = NetworkMetricEntity(
    id = id,
    signalStrength = signalStrength,
    signalQuality = signalQuality,
    networkType = networkType.label,
    latencyMs = latencyMs,
    deviceModel = deviceModel,
    androidVersion = androidVersion,
    recordedAt = recordedAt,
    synced = synced
)

fun QoeFeedbackEntity.toDomain(): QoeFeedback = QoeFeedback(
    id = id,
    overallRating = overallRating,
    responsivenessRating = responsivenessRating,
    streamingRating = streamingRating,
    callQualityRating = callQualityRating,
    satisfactionRating = satisfactionRating,
    triggerEvent = triggerEvent,
    networkType = networkType?.let { NetworkType.fromLabel(it) },
    networkMetricId = networkMetricId,
    metricRecordedAt = metricRecordedAt,
    signalStrengthSnapshot = signalStrengthSnapshot,
    latencyMsSnapshot = latencyMsSnapshot,
    notes = notes,
    recordedAt = recordedAt,
    synced = synced
)

fun QoeFeedback.toEntity(): QoeFeedbackEntity = QoeFeedbackEntity(
    id = id,
    overallRating = overallRating,
    responsivenessRating = responsivenessRating,
    streamingRating = streamingRating,
    callQualityRating = callQualityRating,
    satisfactionRating = satisfactionRating,
    triggerEvent = triggerEvent,
    networkType = networkType?.label,
    networkMetricId = networkMetricId,
    metricRecordedAt = metricRecordedAt,
    signalStrengthSnapshot = signalStrengthSnapshot,
    latencyMsSnapshot = latencyMsSnapshot,
    notes = notes,
    recordedAt = recordedAt,
    synced = synced
)
