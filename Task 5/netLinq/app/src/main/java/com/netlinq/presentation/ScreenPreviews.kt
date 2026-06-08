package com.netlinq.presentation

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.NetworkType
import com.netlinq.presentation.feedback.FeedbackScreen
import com.netlinq.presentation.feedback.FeedbackUiState
import com.netlinq.presentation.home.DashboardScreen
import com.netlinq.presentation.home.HomeUiState
import com.netlinq.presentation.settings.SettingsScreen
import com.netlinq.presentation.settings.SettingsUiState
import com.netlinq.ui.theme.NetLinqTheme

@Preview(name = "Dashboard", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewDashboard() {
    val metric = NetworkMetric(
        signalStrength = -91,
        signalQuality = -91,
        networkType = NetworkType.WIFI,
        latencyMs = 931,
        deviceModel = "Pixel",
        androidVersion = "14",
        recordedAt = System.currentTimeMillis()
    )
    NetLinqTheme {
        Surface {
            DashboardScreen(
                uiState = HomeUiState(
                    latestMetric = metric,
                    recentMetrics = listOf(metric),
                    pendingSyncCount = 3
                ),
                onCheckNetwork = {},
                onSync = {}
            )
        }
    }
}

@Preview(name = "Feedback form", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewFeedbackForm() {
    NetLinqTheme {
        Surface {
            FeedbackScreen(
                uiState = FeedbackUiState(
                    overall = 4,
                    responsiveness = 3,
                    networkContext = "WiFi"
                ),
                onOverallChange = {},
                onResponsivenessChange = {},
                onStreamingChange = {},
                onCallQualityChange = {},
                onSatisfactionChange = {},
                onNotesChange = {},
                onSubmit = {}
            )
        }
    }
}

@Preview(name = "Settings", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSettings() {
    NetLinqTheme {
        Surface {
            SettingsScreen(
                uiState = SettingsUiState(
                    monitoringEnabled = true,
                    wifiOnlySync = true
                ),
                onMonitoringChange = {},
                onWifiOnlyChange = {},
                onFrequencyChange = {},
                onTriggerSignalChange = {},
                onTriggerNetworkChange = {},
                onTriggerLatencyChange = {},
                onTriggerConnectivityChange = {},
                onThemeModeChange = {}
            )
        }
    }
}
