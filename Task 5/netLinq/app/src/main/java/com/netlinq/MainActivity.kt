package com.netlinq

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.netlinq.data.preferences.AppPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.netlinq.monitoring.NetworkPromptEvent
import com.netlinq.notifications.FeedbackNotificationHelper
import com.netlinq.presentation.MainViewModel
import com.netlinq.presentation.monitoring.PromptEventViewModel
import com.netlinq.presentation.navigation.NetLinqNavGraph
import com.netlinq.ui.theme.NetLinqTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var pendingPrompt by mutableStateOf<NetworkPromptEvent?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pendingPrompt = readPromptFromIntent(intent)
        enableEdgeToEdge()
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val promptViewModel: PromptEventViewModel = hiltViewModel()
            val startDestination by mainViewModel.startDestination.collectAsState()
            val themeMode by mainViewModel.themeMode.collectAsState()
            val useDarkTheme = when (themeMode) {
                AppPreferences.THEME_MODE_LIGHT -> false
                AppPreferences.THEME_MODE_DARK -> true
                else -> isSystemInDarkTheme()
            }

            NetLinqTheme(darkTheme = useDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NetLinqNavGraph(
                        startDestination = startDestination,
                        promptEventViewModel = promptViewModel,
                        initialPromptEvent = pendingPrompt
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingPrompt = readPromptFromIntent(intent)
    }

    private fun readPromptFromIntent(intent: Intent?): NetworkPromptEvent? {
        intent ?: return null
        return PromptEventViewModel.parsePendingIntent(
            triggerName = intent.getStringExtra(FeedbackNotificationHelper.EXTRA_TRIGGER),
            networkType = intent.getStringExtra(FeedbackNotificationHelper.EXTRA_NETWORK_TYPE),
            detail = intent.getStringExtra(FeedbackNotificationHelper.EXTRA_DETAIL),
            metricId = intent.getLongExtra(FeedbackNotificationHelper.EXTRA_METRIC_ID, -1L)
                .takeIf { it >= 0 },
            metricRecordedAt = intent.getLongExtra(FeedbackNotificationHelper.EXTRA_METRIC_RECORDED_AT, -1L)
                .takeIf { it >= 0 },
            signalStrength = intent.getIntExtra(FeedbackNotificationHelper.EXTRA_SIGNAL_STRENGTH, Int.MIN_VALUE)
                .takeIf { it != Int.MIN_VALUE },
            latencyMs = intent.getIntExtra(FeedbackNotificationHelper.EXTRA_LATENCY_MS, Int.MIN_VALUE)
                .takeIf { it != Int.MIN_VALUE }
        )
    }
}
