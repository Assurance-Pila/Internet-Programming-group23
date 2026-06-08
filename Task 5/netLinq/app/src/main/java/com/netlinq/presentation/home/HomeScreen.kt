package com.netlinq.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.presentation.components.MetricStatChip
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.QualityBadge
import com.netlinq.presentation.components.SectionHeader
import com.netlinq.presentation.components.TrendChartPlaceholder
import com.netlinq.presentation.util.latencyQualityLevel
import com.netlinq.presentation.util.overallQuality
import com.netlinq.presentation.util.signalQualityLevel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    uiState: HomeUiState,
    onCheckNetwork: () -> Unit,
    onSync: () -> Unit
) {
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
            title = "Network dashboard",
            subtitle = "Automatic readings from your phone: signal, speed, and connection type."
        )

        NetworkStatusHero(metric = uiState.latestMetric)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onCheckNetwork,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isCollecting
            ) {
                if (uiState.isCollecting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.NetworkCheck, contentDescription = "Check network now")
                        Text("Check now", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
            OutlinedButton(
                onClick = onSync,
                modifier = Modifier.weight(1f),
                enabled = !uiState.isSyncing
            ) {
                if (uiState.isSyncing) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CloudSync, contentDescription = "Sync data")
                        Text("Sync", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }

        uiState.statusMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (uiState.pendingSyncCount > 0) {
            NetLinqCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${uiState.pendingSyncCount} reading(s) waiting to upload",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        TrendChartPlaceholder(
            values = uiState.recentMetrics
                .mapNotNull { it.latencyMs }
                .reversed(),
            title = "Speed trend"
        )

        if (uiState.recentMetrics.isNotEmpty()) {
            SectionHeader(
                title = "Recent readings",
                subtitle = "Go to Feedback anytime to rate how it felt."
            )
            uiState.recentMetrics.forEach { metric ->
                CompactMetricRow(metric = metric)
            }
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Quick rating prompts",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "When signal drops, internet slows down, or your network changes, " +
                        "we ask you to rate your experience. In the app or by notification.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun NetworkStatusHero(metric: NetworkMetric?) {
    val quality = overallQuality(metric)
    val isEmpty = metric == null

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = if (isEmpty) 40.dp else 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(if (isEmpty) 88.dp else 72.dp)
                    .clip(CircleShape)
                    .background(
                        (if (isEmpty) MaterialTheme.colorScheme.primary else quality.color)
                            .copy(alpha = 0.18f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.SignalCellularAlt,
                    contentDescription = null,
                    tint = if (isEmpty) MaterialTheme.colorScheme.primary else quality.color,
                    modifier = Modifier.size(if (isEmpty) 44.dp else 36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isEmpty) {
                Text(
                    text = "Your network",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Tap Check now below",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "We measure signal, speed, and connection type for you.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, start = 16.dp, end = 16.dp)
                )
            } else {
                Text(
                    text = "Right now",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = metric.networkType.label,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp)
                )
                QualityBadge(
                    level = quality,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MetricStatChip(
                        label = "Signal",
                        value = metric.signalStrength?.let { formatSignal(it) } ?: "N/A",
                        accent = signalQualityLevel(metric.signalStrength).color
                    )
                    MetricStatChip(
                        label = "Speed",
                        value = metric.latencyMs?.let { "$it ms" } ?: "N/A",
                        accent = latencyQualityLevel(metric.latencyMs).color
                    )
                    MetricStatChip(
                        label = "Type",
                        value = metric.networkType.label,
                        accent = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun CompactMetricRow(metric: NetworkMetric) {
    val quality = overallQuality(metric)
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = formatTime(metric.recordedAt),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${metric.networkType.label}, ${metric.latencyMs?.let { "$it ms" } ?: "no speed reading"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            QualityBadge(level = quality)
        }
    }
}

private fun formatSignal(dbm: Int): String = when {
    dbm >= -70 -> "Strong"
    dbm >= -85 -> "OK"
    else -> "Weak"
}

private fun formatTime(epochMillis: Long): String {
    val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    return formatter.format(Date(epochMillis))
}
