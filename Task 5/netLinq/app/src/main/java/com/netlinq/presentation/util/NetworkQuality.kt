package com.netlinq.presentation.util

import androidx.compose.ui.graphics.Color
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.ui.theme.QualityFair
import com.netlinq.ui.theme.QualityGood
import com.netlinq.ui.theme.QualityPoor

enum class QualityLevel(val label: String, val color: Color) {
    GOOD("Good", QualityGood),
    FAIR("Fair", QualityFair),
    POOR("Poor", QualityPoor),
    UNKNOWN("N/A", QualityFair)
}

fun signalQualityLevel(dbm: Int?): QualityLevel = when {
    dbm == null -> QualityLevel.UNKNOWN
    dbm >= -70 -> QualityLevel.GOOD
    dbm >= -85 -> QualityLevel.FAIR
    else -> QualityLevel.POOR
}

fun latencyQualityLevel(ms: Int?): QualityLevel = when {
    ms == null -> QualityLevel.UNKNOWN
    ms < 100 -> QualityLevel.GOOD
    ms <= 300 -> QualityLevel.FAIR
    else -> QualityLevel.POOR
}

fun overallQuality(metric: NetworkMetric?): QualityLevel {
    if (metric == null) return QualityLevel.UNKNOWN
    val signal = signalQualityLevel(metric.signalStrength)
    val latency = latencyQualityLevel(metric.latencyMs)
    val levels = listOf(signal, latency).filter { it != QualityLevel.UNKNOWN }
    if (levels.isEmpty()) return QualityLevel.UNKNOWN
    return levels.maxBy { it.ordinal }
}
