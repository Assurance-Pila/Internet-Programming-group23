package com.netlinq.presentation.onboarding

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.netlinq.presentation.onboarding.steps.ConsentStepContent
import com.netlinq.presentation.onboarding.steps.HowItWorksStepContent
import com.netlinq.presentation.onboarding.steps.PermissionsStepContent
import com.netlinq.presentation.onboarding.steps.SetupCompleteStepContent
import com.netlinq.presentation.onboarding.steps.WelcomeStepContent
import com.netlinq.ui.theme.NetLinqTheme

@Preview(name = "1 · Welcome", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewWelcomeStep() {
    NetLinqTheme {
        Surface {
            WelcomeStepContent(modifier = Modifier.padding(24.dp))
        }
    }
}

@Preview(name = "2 · How it works", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewHowItWorksStep() {
    NetLinqTheme {
        Surface {
            HowItWorksStepContent(modifier = Modifier.padding(24.dp))
        }
    }
}

@Preview(name = "3 · Consent", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewConsentStep() {
    NetLinqTheme {
        Surface {
            ConsentStepContent(
                consentMonitoring = true,
                consentNotifications = true,
                consentSync = false,
                onMonitoringChange = {},
                onNotificationsChange = {},
                onSyncChange = {},
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Preview(name = "4 · Permissions", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPermissionsStep() {
    NetLinqTheme {
        Surface {
            PermissionsStepContent(
                permissionsGranted = false,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Preview(name = "5 · Setup complete", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewSetupCompleteStep() {
    NetLinqTheme {
        Surface {
            SetupCompleteStepContent(
                deviceIdPreview = "a3f8b2c1…",
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}

@Preview(name = "Full flow · Consent step", showBackground = true, showSystemUi = true)
@Composable
private fun PreviewOnboardingScreen() {
    NetLinqTheme {
        Surface {
            OnboardingScreen(
                uiState = OnboardingUiState(
                    step = OnboardingStep.CONSENT,
                    deviceIdPreview = "a3f8b2c1…",
                    consentMonitoring = true,
                    consentNotifications = false,
                    consentSync = false,
                    isLoading = false
                ),
                onConsentMonitoringChange = {},
                onConsentNotificationsChange = {},
                onConsentSyncChange = {},
                onRequestPermissions = {},
                onNext = {},
                onBack = {},
                onComplete = {}
            )
        }
    }
}
