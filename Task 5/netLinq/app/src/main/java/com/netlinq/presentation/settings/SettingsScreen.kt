package com.netlinq.presentation.settings

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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.netlinq.data.preferences.AppPreferences
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.SectionHeader

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onMonitoringChange: (Boolean) -> Unit,
    onWifiOnlyChange: (Boolean) -> Unit,
    onFrequencyChange: (Int) -> Unit,
    onTriggerSignalChange: (Boolean) -> Unit,
    onTriggerNetworkChange: (Boolean) -> Unit,
    onTriggerLatencyChange: (Boolean) -> Unit,
    onTriggerConnectivityChange: (Boolean) -> Unit,
    onThemeModeChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        SectionHeader(
            title = "Settings",
            subtitle = "Control monitoring, sync, and feedback frequency."
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            SettingToggleRow(
                title = "Network monitoring",
                description = "Check signal, speed, and connection type in the background.",
                checked = uiState.monitoringEnabled,
                onCheckedChange = onMonitoringChange,
                switchDescription = "Toggle network monitoring"
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingToggleRow(
                title = "WiFi-only sync",
                description = "Upload data only on WiFi to save mobile data.",
                checked = uiState.wifiOnlySync,
                onCheckedChange = onWifiOnlyChange,
                switchDescription = "Toggle WiFi only sync"
            )
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Monitoring triggers",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                SettingToggleRow(
                    title = "Signal drop",
                    description = "Prompt when signal weakens significantly.",
                    checked = uiState.triggerSignalDrop,
                    onCheckedChange = onTriggerSignalChange,
                    switchDescription = "Toggle signal drop prompts"
                )
                SettingToggleRow(
                    title = "Network change",
                    description = "Prompt when switching between 2G, 3G, 4G, 5G, or WiFi.",
                    checked = uiState.triggerNetworkChange,
                    onCheckedChange = onTriggerNetworkChange,
                    switchDescription = "Toggle network change prompts"
                )
                SettingToggleRow(
                    title = "Slow internet",
                    description = "Ask you to rate when speed drops sharply.",
                    checked = uiState.triggerLatencySpike,
                    onCheckedChange = onTriggerLatencyChange,
                    switchDescription = "Toggle slow internet prompts"
                )
                SettingToggleRow(
                    title = "Connection loss",
                    description = "Prompt when going offline or reconnecting.",
                    checked = uiState.triggerConnectivity,
                    onCheckedChange = onTriggerConnectivityChange,
                    switchDescription = "Toggle connection loss prompts"
                )
            }
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Feedback frequency", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "How often we ask you to rate your experience when issues are detected.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                FrequencyOption("Less often", uiState.feedbackFrequency == AppPreferences.FEEDBACK_FREQUENCY_LOW) {
                    onFrequencyChange(AppPreferences.FEEDBACK_FREQUENCY_LOW)
                }
                FrequencyOption("Normal", uiState.feedbackFrequency == AppPreferences.FEEDBACK_FREQUENCY_NORMAL) {
                    onFrequencyChange(AppPreferences.FEEDBACK_FREQUENCY_NORMAL)
                }
                FrequencyOption("More often", uiState.feedbackFrequency == AppPreferences.FEEDBACK_FREQUENCY_HIGH) {
                    onFrequencyChange(AppPreferences.FEEDBACK_FREQUENCY_HIGH)
                }
            }
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Appearance", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Choose light, dark, or follow your phone setting.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                ThemeOption(
                    label = "System default",
                    selected = uiState.themeMode == AppPreferences.THEME_MODE_SYSTEM,
                    onSelect = { onThemeModeChange(AppPreferences.THEME_MODE_SYSTEM) }
                )
                ThemeOption(
                    label = "Light",
                    selected = uiState.themeMode == AppPreferences.THEME_MODE_LIGHT,
                    onSelect = { onThemeModeChange(AppPreferences.THEME_MODE_LIGHT) }
                )
                ThemeOption(
                    label = "Dark",
                    selected = uiState.themeMode == AppPreferences.THEME_MODE_DARK,
                    onSelect = { onThemeModeChange(AppPreferences.THEME_MODE_DARK) }
                )
            }
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Privacy", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "No personal data is collected. Your device ID is scrambled before upload. " +
                        "You can stop monitoring anytime.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun ThemeOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    switchDescription: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.semantics { contentDescription = switchDescription }
        )
    }
}

@Composable
private fun FrequencyOption(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onSelect, role = Role.RadioButton)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(text = label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(start = 8.dp))
    }
}
