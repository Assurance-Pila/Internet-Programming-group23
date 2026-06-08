package com.netlinq.presentation.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.netlinq.domain.model.FeedbackNetworkLink
import com.netlinq.domain.model.NetworkType
import com.netlinq.monitoring.NetworkPromptEvent
import com.netlinq.presentation.components.NetLinqBottomBar
import com.netlinq.presentation.components.NetLinqNavItem
import com.netlinq.presentation.feedback.FeedbackPromptHost
import com.netlinq.presentation.feedback.FeedbackPromptUiState
import com.netlinq.presentation.feedback.FeedbackScreen
import com.netlinq.presentation.feedback.FeedbackViewModel
import com.netlinq.presentation.history.HistoryScreen
import com.netlinq.presentation.history.HistoryViewModel
import com.netlinq.presentation.home.DashboardScreen
import com.netlinq.presentation.home.HomeViewModel
import com.netlinq.presentation.monitoring.PromptEventViewModel
import com.netlinq.presentation.navigation.Routes
import com.netlinq.presentation.settings.SettingsScreen
import com.netlinq.presentation.settings.SettingsViewModel

private val bottomNavItems = listOf(
    NetLinqNavItem(Routes.DASHBOARD, "Dashboard", Icons.Default.CellTower),
    NetLinqNavItem(Routes.HISTORY, "History", Icons.Default.History),
    NetLinqNavItem(Routes.FEEDBACK, "Feedback", Icons.Default.RateReview),
    NetLinqNavItem(Routes.SETTINGS, "Settings", Icons.Default.Settings)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    promptEventViewModel: PromptEventViewModel = hiltViewModel(),
    initialPromptEvent: NetworkPromptEvent? = null
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val feedbackViewModel: FeedbackViewModel = hiltViewModel()

    var promptState by remember { mutableStateOf<FeedbackPromptUiState?>(null) }

    fun showPrompt(event: NetworkPromptEvent) {
        promptState = FeedbackPromptUiState(
            trigger = event.trigger,
            networkType = event.networkType,
            detail = event.detail,
            metricId = event.metricId,
            metricRecordedAt = event.metricRecordedAt,
            signalStrength = event.signalStrength,
            latencyMs = event.latencyMs
        )
    }

    LaunchedEffect(initialPromptEvent) {
        initialPromptEvent?.let { showPrompt(it) }
    }

    LaunchedEffect(promptEventViewModel) {
        promptEventViewModel.foregroundPrompts.collect { event ->
            showPrompt(event)
        }
    }

    FeedbackPromptHost(
        prompt = promptState,
        onDismiss = { promptState = null },
        onRatingChange = { rating -> promptState = promptState?.copy(overallRating = rating) },
        onSubmit = {
            val current = promptState ?: return@FeedbackPromptHost
            feedbackViewModel.submitQuickFeedback(
                trigger = current.trigger,
                overall = current.overallRating,
                link = current.toNetworkLink()
            )
            promptState = null
        },
        onOpenFullFeedback = {
            navController.navigate(Routes.FEEDBACK) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NetLinqBottomBar(
                items = bottomNavItems,
                currentRoute = currentRoute,
                onItemSelected = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DASHBOARD) {
                val viewModel: HomeViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                DashboardScreen(
                    uiState = uiState,
                    onCheckNetwork = viewModel::collectNow,
                    onSync = viewModel::syncNow
                )
            }
            composable(Routes.HISTORY) {
                val viewModel: HistoryViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                HistoryScreen(uiState = uiState)
            }
            composable(Routes.FEEDBACK) {
                val uiState by feedbackViewModel.uiState.collectAsState()
                FeedbackScreen(
                    uiState = uiState,
                    onOverallChange = feedbackViewModel::setOverall,
                    onResponsivenessChange = feedbackViewModel::setResponsiveness,
                    onStreamingChange = feedbackViewModel::setStreaming,
                    onCallQualityChange = feedbackViewModel::setCallQuality,
                    onSatisfactionChange = feedbackViewModel::setSatisfaction,
                    onNotesChange = feedbackViewModel::setNotes,
                    onSubmit = feedbackViewModel::submit
                )
            }
            composable(Routes.SETTINGS) {
                val viewModel: SettingsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                SettingsScreen(
                    uiState = uiState,
                    onMonitoringChange = viewModel::setMonitoring,
                    onWifiOnlyChange = viewModel::setWifiOnlySync,
                    onFrequencyChange = viewModel::setFeedbackFrequency,
                    onTriggerSignalChange = viewModel::setTriggerSignalDrop,
                    onTriggerNetworkChange = viewModel::setTriggerNetworkChange,
                    onTriggerLatencyChange = viewModel::setTriggerLatencySpike,
                    onTriggerConnectivityChange = viewModel::setTriggerConnectivity,
                    onThemeModeChange = viewModel::setThemeMode
                )
            }
        }
    }
}

private fun FeedbackPromptUiState.toNetworkLink(): FeedbackNetworkLink =
    FeedbackNetworkLink(
        metricId = metricId,
        networkType = NetworkType.fromLabel(networkType),
        signalStrength = signalStrength,
        latencyMs = latencyMs,
        metricRecordedAt = metricRecordedAt
    )
