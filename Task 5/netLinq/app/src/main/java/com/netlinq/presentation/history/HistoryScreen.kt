package com.netlinq.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.netlinq.domain.model.QoeFeedback
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.SectionHeader
import com.netlinq.presentation.components.TrendChartPlaceholder
import com.netlinq.presentation.util.overallQuality
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(uiState: HistoryUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            title = "Your history",
            subtitle = "Automatic readings and your star ratings over time."
        )

        if (uiState.averageQoe > 0f) {
            NetLinqCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Average satisfaction",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f / 5", uiState.averageQoe),
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.semantics {
                            contentDescription = "Average satisfaction score ${uiState.averageQoe} out of 5"
                        }
                    )
                }
            }
        }

        TrendChartPlaceholder(
            values = uiState.metrics.mapNotNull { it.latencyMs }.reversed(),
            title = "Speed trend",
            modifier = Modifier.fillMaxWidth()
        )

        SignalTrendChart(
            values = uiState.metrics.mapNotNull { it.signalStrength }.reversed()
        )

        if (uiState.networkDistribution.isNotEmpty()) {
            NetworkDistributionCard(distribution = uiState.networkDistribution)
        }

        if (uiState.feedback.isNotEmpty()) {
            SectionHeader(title = "Recent feedback")
            uiState.feedback.forEach { item ->
                FeedbackEventRow(item)
            }
        }

        if (uiState.metrics.isNotEmpty()) {
            SectionHeader(title = "Recent readings")
            uiState.metrics.take(5).forEach { metric ->
                val quality = overallQuality(metric)
                NetLinqCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(formatTime(metric.recordedAt), style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${metric.networkType.label}, ${metric.latencyMs?.let { "$it ms" } ?: "no speed reading"}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(quality.label, color = quality.color, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun SignalTrendChart(values: List<Int>) {
    val max = (values.maxOrNull() ?: -50).coerceAtLeast(-50)
    val min = (values.minOrNull() ?: -100).coerceAtMost(-100)
    val range = (max - min).coerceAtLeast(1)

    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Signal trend", style = MaterialTheme.typography.titleMedium)
            Text(
                "Last ${values.size} readings",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )
            if (values.isEmpty()) {
                Text("No signal data yet.", style = MaterialTheme.typography.bodyMedium)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    values.forEach { dbm ->
                        val fraction = (dbm - min).toFloat() / range.toFloat()
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 3.dp)
                                .height((fraction * 80).dp.coerceAtLeast(8.dp))
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NetworkDistributionCard(distribution: Map<String, Int>) {
    val total = distribution.values.sum().coerceAtLeast(1)
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("Network type distribution", style = MaterialTheme.typography.titleMedium)
            distribution.forEach { (type, count) ->
                val fraction = count.toFloat() / total.toFloat()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(type, modifier = Modifier.weight(0.3f), style = MaterialTheme.typography.bodyMedium)
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .height(12.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = fraction.coerceIn(0.15f, 1f)))
                    )
                    Text(
                        "${(fraction * 100).toInt()}%",
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(start = 8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun FeedbackEventRow(feedback: QoeFeedback) {
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(formatTime(feedback.recordedAt), style = MaterialTheme.typography.titleMedium)
                Text(
                    formatFeedbackContext(feedback),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "${feedback.overallRating}/5",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun formatFeedbackContext(feedback: QoeFeedback): String {
    val trigger = when (feedback.triggerEvent) {
        "manual" -> "You rated manually"
        null -> "Rating"
        else -> feedback.triggerEvent.replace('_', ' ')
    }
    val network = feedback.networkType?.label ?: "unknown network"
    val speed = feedback.latencyMsSnapshot?.let { "$it ms" }
    val linked = feedback.metricRecordedAt?.let { "linked to reading at ${formatTime(it)}" }
    return listOfNotNull(trigger, network, speed, linked).joinToString(", ")
}

private fun formatTime(epochMillis: Long): String {
    val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    return formatter.format(Date(epochMillis))
}
