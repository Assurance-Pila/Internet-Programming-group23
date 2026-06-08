package com.netlinq.presentation.onboarding

enum class OnboardingStep(val index: Int, val title: String) {
    WELCOME(1, "Welcome"),
    HOW_IT_WORKS(2, "How it works"),
    CONSENT(3, "Your consent"),
    PERMISSIONS(4, "Permissions"),
    COMPLETE(5, "Ready");

    fun next(): OnboardingStep? = entries.getOrNull(ordinal + 1)
    fun previous(): OnboardingStep? = entries.getOrNull(ordinal - 1)
}

data class OnboardingUiState(
    val step: OnboardingStep = OnboardingStep.WELCOME,
    val deviceIdPreview: String = "",
    val consentMonitoring: Boolean = false,
    val consentNotifications: Boolean = false,
    val consentSync: Boolean = false,
    val permissionsGranted: Boolean = false,
    val isLoading: Boolean = true
) {
    val consentComplete: Boolean =
        consentMonitoring && consentNotifications && consentSync
}
