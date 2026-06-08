package com.netlinq.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.preferences.AppPreferences
import com.netlinq.data.repository.DeviceRepository
import com.netlinq.sync.SyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
    private val appPreferences: AppPreferences,
    private val syncScheduler: SyncScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val deviceId = deviceRepository.getOrCreateDeviceId()
            _uiState.update {
                it.copy(
                    deviceIdPreview = deviceId.take(8) + "…",
                    isLoading = false
                )
            }
        }
    }

    fun goNext() {
        _uiState.update { state ->
            state.copy(step = state.step.next() ?: state.step)
        }
    }

    fun goBack() {
        _uiState.update { state ->
            state.copy(step = state.step.previous() ?: state.step)
        }
    }

    fun setConsentMonitoring(value: Boolean) {
        _uiState.update { it.copy(consentMonitoring = value) }
    }

    fun setConsentNotifications(value: Boolean) {
        _uiState.update { it.copy(consentNotifications = value) }
    }

    fun setConsentSync(value: Boolean) {
        _uiState.update { it.copy(consentSync = value) }
    }

    fun onPermissionsResult(granted: Boolean) {
        _uiState.update { it.copy(permissionsGranted = granted) }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            appPreferences.completeOnboarding()
            syncScheduler.schedulePeriodicSync(wifiOnly = true)
        }
    }
}
