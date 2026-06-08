package com.netlinq.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.netlinq.monitoring.NetworkDegradationDetector
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "netlinq_prefs")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    val deviceId: Flow<String> = dataStore.data.map { prefs ->
        prefs[KEY_DEVICE_ID] ?: ""
    }

    val onboardingComplete: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_ONBOARDING_COMPLETE] ?: false
    }

    val monitoringEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_MONITORING_ENABLED] ?: false
    }

    val wifiOnlySync: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_WIFI_ONLY_SYNC] ?: true
    }

    val feedbackFrequency: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_FEEDBACK_FREQUENCY] ?: FEEDBACK_FREQUENCY_NORMAL
    }

    val triggerSignalDrop: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_TRIGGER_SIGNAL] ?: true
    }

    val triggerNetworkChange: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_TRIGGER_NETWORK] ?: true
    }

    val triggerLatencySpike: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_TRIGGER_LATENCY] ?: true
    }

    val triggerConnectivity: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[KEY_TRIGGER_CONNECTIVITY] ?: true
    }

    val themeMode: Flow<Int> = dataStore.data.map { prefs ->
        prefs[KEY_THEME_MODE] ?: THEME_MODE_SYSTEM
    }

    suspend fun ensureDeviceId(): String {
        var id = ""
        dataStore.edit { prefs ->
            id = prefs[KEY_DEVICE_ID] ?: UUID.randomUUID().toString().also { newId ->
                prefs[KEY_DEVICE_ID] = newId
            }
        }
        return id
    }

    suspend fun completeOnboarding() {
        dataStore.edit { prefs ->
            prefs[KEY_ONBOARDING_COMPLETE] = true
            prefs[KEY_MONITORING_ENABLED] = true
        }
    }

    suspend fun setMonitoringEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_MONITORING_ENABLED] = enabled
        }
    }

    suspend fun setWifiOnlySync(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[KEY_WIFI_ONLY_SYNC] = enabled
        }
    }

    suspend fun setFeedbackFrequency(frequency: Int) {
        dataStore.edit { prefs ->
            prefs[KEY_FEEDBACK_FREQUENCY] = frequency
        }
    }

    suspend fun setTriggerSignalDrop(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_TRIGGER_SIGNAL] = enabled }
    }

    suspend fun setTriggerNetworkChange(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_TRIGGER_NETWORK] = enabled }
    }

    suspend fun setTriggerLatencySpike(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_TRIGGER_LATENCY] = enabled }
    }

    suspend fun setTriggerConnectivity(enabled: Boolean) {
        dataStore.edit { prefs -> prefs[KEY_TRIGGER_CONNECTIVITY] = enabled }
    }

    suspend fun setThemeMode(mode: Int) {
        dataStore.edit { prefs -> prefs[KEY_THEME_MODE] = mode }
    }

    suspend fun getTriggerSettings(): NetworkDegradationDetector.TriggerSettings {
        val prefs = dataStore.data.first()
        return NetworkDegradationDetector.TriggerSettings(
            signalDropEnabled = prefs[KEY_TRIGGER_SIGNAL] ?: true,
            networkChangeEnabled = prefs[KEY_TRIGGER_NETWORK] ?: true,
            latencySpikeEnabled = prefs[KEY_TRIGGER_LATENCY] ?: true,
            connectivityEnabled = prefs[KEY_TRIGGER_CONNECTIVITY] ?: true
        )
    }

    companion object {
        const val FEEDBACK_FREQUENCY_LOW = 0
        const val FEEDBACK_FREQUENCY_NORMAL = 1
        const val FEEDBACK_FREQUENCY_HIGH = 2

        const val THEME_MODE_SYSTEM = 0
        const val THEME_MODE_LIGHT = 1
        const val THEME_MODE_DARK = 2

        private val KEY_DEVICE_ID = stringPreferencesKey("device_id")
        private val KEY_ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
        private val KEY_MONITORING_ENABLED = booleanPreferencesKey("monitoring_enabled")
        private val KEY_WIFI_ONLY_SYNC = booleanPreferencesKey("wifi_only_sync")
        private val KEY_FEEDBACK_FREQUENCY = intPreferencesKey("feedback_frequency")
        private val KEY_TRIGGER_SIGNAL = booleanPreferencesKey("trigger_signal")
        private val KEY_TRIGGER_NETWORK = booleanPreferencesKey("trigger_network")
        private val KEY_TRIGGER_LATENCY = booleanPreferencesKey("trigger_latency")
        private val KEY_TRIGGER_CONNECTIVITY = booleanPreferencesKey("trigger_connectivity")
        private val KEY_THEME_MODE = intPreferencesKey("theme_mode")
    }
}
