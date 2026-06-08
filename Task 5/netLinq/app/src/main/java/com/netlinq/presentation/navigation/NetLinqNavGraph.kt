package com.netlinq.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.netlinq.monitoring.NetworkPromptEvent
import com.netlinq.presentation.monitoring.PromptEventViewModel
import com.netlinq.presentation.onboarding.OnboardingScreen
import com.netlinq.presentation.onboarding.OnboardingViewModel
import com.netlinq.presentation.shell.MainScaffold

object Routes {
    const val ONBOARDING = "onboarding"
    const val MAIN = "main"
    const val DASHBOARD = "dashboard"
    const val HISTORY = "history"
    const val FEEDBACK = "feedback"
    const val SETTINGS = "settings"
    const val OPERATOR = "operator"
}

@Composable
fun NetLinqNavGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
    promptEventViewModel: PromptEventViewModel? = null,
    initialPromptEvent: NetworkPromptEvent? = null
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.ONBOARDING) {
            val viewModel: OnboardingViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            OnboardingScreen(
                uiState = uiState,
                onConsentMonitoringChange = viewModel::setConsentMonitoring,
                onConsentNotificationsChange = viewModel::setConsentNotifications,
                onConsentSyncChange = viewModel::setConsentSync,
                onRequestPermissions = viewModel::onPermissionsResult,
                onNext = viewModel::goNext,
                onBack = viewModel::goBack,
                onComplete = {
                    viewModel.completeOnboarding()
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.MAIN) {
            val promptVm = promptEventViewModel ?: hiltViewModel<PromptEventViewModel>()
            MainScaffold(
                promptEventViewModel = promptVm,
                initialPromptEvent = initialPromptEvent
            )
        }
    }
}
