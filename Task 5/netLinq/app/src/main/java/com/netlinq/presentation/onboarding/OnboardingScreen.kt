package com.netlinq.presentation.onboarding

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netlinq.presentation.components.StepIndicator
import com.netlinq.presentation.onboarding.steps.ConsentStepContent
import com.netlinq.presentation.onboarding.steps.HowItWorksStepContent
import com.netlinq.presentation.onboarding.steps.PermissionsStepContent
import com.netlinq.presentation.onboarding.steps.SetupCompleteStepContent
import com.netlinq.presentation.onboarding.steps.WelcomeStepContent

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    onConsentMonitoringChange: (Boolean) -> Unit,
    onConsentNotificationsChange: (Boolean) -> Unit,
    onConsentSyncChange: (Boolean) -> Unit,
    onRequestPermissions: (Boolean) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        onRequestPermissions(results.values.all { it })
    }

    val requiredPermissions = buildList {
        add(Manifest.permission.READ_PHONE_STATE)
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val canGoNext = when (uiState.step) {
        OnboardingStep.WELCOME, OnboardingStep.HOW_IT_WORKS -> true
        OnboardingStep.CONSENT -> uiState.consentComplete
        OnboardingStep.PERMISSIONS -> uiState.permissionsGranted
        OnboardingStep.COMPLETE -> !uiState.isLoading
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .padding(top = 24.dp, bottom = 16.dp)
    ) {
        StepIndicator(
            currentStep = uiState.step.index,
            totalSteps = OnboardingStep.entries.size,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Step ${uiState.step.index} of ${OnboardingStep.entries.size}: ${uiState.step.title}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (uiState.step) {
                OnboardingStep.WELCOME -> WelcomeStepContent()
                OnboardingStep.HOW_IT_WORKS -> HowItWorksStepContent()
                OnboardingStep.CONSENT -> ConsentStepContent(
                    consentMonitoring = uiState.consentMonitoring,
                    consentNotifications = uiState.consentNotifications,
                    consentSync = uiState.consentSync,
                    onMonitoringChange = onConsentMonitoringChange,
                    onNotificationsChange = onConsentNotificationsChange,
                    onSyncChange = onConsentSyncChange
                )
                OnboardingStep.PERMISSIONS -> PermissionsStepContent(
                    permissionsGranted = uiState.permissionsGranted
                )
                OnboardingStep.COMPLETE -> SetupCompleteStepContent(
                    deviceIdPreview = uiState.deviceIdPreview
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (uiState.step) {
            OnboardingStep.PERMISSIONS -> {
                if (!uiState.permissionsGranted) {
                    OutlinedButton(
                        onClick = { permissionLauncher.launch(requiredPermissions.toTypedArray()) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.consentComplete
                    ) {
                        Text("Grant permissions")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            else -> Unit
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.step != OnboardingStep.WELCOME) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Back")
                }
            }

            if (uiState.step == OnboardingStep.COMPLETE) {
                Button(
                    onClick = onComplete,
                    modifier = Modifier.weight(1f),
                    enabled = canGoNext
                ) {
                    Text("Open dashboard")
                }
            } else {
                Button(
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    enabled = canGoNext
                ) {
                    Text("Continue")
                }
            }
        }
    }
}
