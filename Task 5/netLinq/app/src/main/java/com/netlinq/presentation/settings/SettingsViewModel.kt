package com.netlinq.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.preferences.AppPreferences
import com.netlinq.sync.SyncScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val monitoringEnabled: Boolean = true,
    val wifiOnlySync: Boolean = true,
    val feedbackFrequency: Int = AppPreferences.FEEDBACK_FREQUENCY_NORMAL,
    val themeMode: Int = AppPreferences.THEME_MODE_SYSTEM,
    val triggerSignalDrop: Boolean = true,
    val triggerNetworkChange: Boolean = true,
    val triggerLatencySpike: Boolean = true,
    val triggerConnectivity: Boolean = true
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val syncScheduler: SyncScheduler
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        combine(
            appPreferences.monitoringEnabled,
            appPreferences.wifiOnlySync,
            appPreferences.feedbackFrequency,
            appPreferences.themeMode
        ) { monitoring, wifiOnly, frequency, themeMode ->
            listOf(monitoring, wifiOnly, frequency, themeMode)
        },
        combine(
            appPreferences.triggerSignalDrop,
            appPreferences.triggerNetworkChange,
            appPreferences.triggerLatencySpike,
            appPreferences.triggerConnectivity
        ) { signal, network, latency, connectivity ->
            listOf(signal, network, latency, connectivity)
        }
    ) { core, triggers ->
        SettingsUiState(
            monitoringEnabled = core[0] as Boolean,
            wifiOnlySync = core[1] as Boolean,
            feedbackFrequency = core[2] as Int,
            themeMode = core[3] as Int,
            triggerSignalDrop = triggers[0],
            triggerNetworkChange = triggers[1],
            triggerLatencySpike = triggers[2],
            triggerConnectivity = triggers[3]
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState()
    )

    fun setMonitoring(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setMonitoringEnabled(enabled) }
    }

    fun setWifiOnlySync(enabled: Boolean) {
        viewModelScope.launch {
            appPreferences.setWifiOnlySync(enabled)
            syncScheduler.schedulePeriodicSync(wifiOnly = enabled)
        }
    }

    fun setFeedbackFrequency(frequency: Int) {
        viewModelScope.launch { appPreferences.setFeedbackFrequency(frequency) }
    }

    fun setThemeMode(mode: Int) {
        viewModelScope.launch { appPreferences.setThemeMode(mode) }
    }

    fun setTriggerSignalDrop(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setTriggerSignalDrop(enabled) }
    }

    fun setTriggerNetworkChange(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setTriggerNetworkChange(enabled) }
    }

    fun setTriggerLatencySpike(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setTriggerLatencySpike(enabled) }
    }

    fun setTriggerConnectivity(enabled: Boolean) {
        viewModelScope.launch { appPreferences.setTriggerConnectivity(enabled) }
    }
}
