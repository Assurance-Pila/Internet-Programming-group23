package com.netlinq.presentation.onboarding.steps

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netlinq.R
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.SectionHeader

@Composable
fun WelcomeStepContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_netlinq_logo),
                contentDescription = "NetLinq logo",
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "NetLinq",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "See how your mobile internet really feels",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        HighlightCard(
            title = "No sign-up required",
            body = "You never create an account. NetLinq gives your phone a random ID. " +
                "No name, email, or phone number."
        )

        Text(
            text = "Help show what mobile internet is really like for people across Cameroon.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun HowItWorksStepContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(
            title = "How NetLinq works",
            subtitle = "We check your connection and ask how it felt. Simple as that."
        )

        FeatureRow(
            icon = Icons.Default.Sensors,
            title = "Automatic checks",
            body = "Your phone measures signal, connection type (2G, 3G, 4G, 5G, WiFi), and speed."
        )
        FeatureRow(
            icon = Icons.Default.RateReview,
            title = "Your ratings",
            body = "Quick star ratings on calls, streaming, and overall satisfaction when issues appear."
        )
        FeatureRow(
            icon = Icons.Default.CloudOff,
            title = "Works offline",
            body = "Readings are saved on your phone first, then sent when you have internet."
        )
        FeatureRow(
            icon = Icons.Default.Shield,
            title = "Your privacy",
            body = "We never upload your name. Operators only see combined trends, not who you are."
        )
    }
}

@Composable
fun ConsentStepContent(
    consentMonitoring: Boolean,
    consentNotifications: Boolean,
    consentSync: Boolean,
    onMonitoringChange: (Boolean) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onSyncChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(
            title = "Your consent",
            subtitle = "We only start after you agree to each of these. You stay in control."
        )

        ConsentCheckboxRow(
            checked = consentMonitoring,
            onCheckedChange = onMonitoringChange,
            title = "Network monitoring",
            description = "Check signal, speed, and connection type while you use the app."
        )
        ConsentCheckboxRow(
            checked = consentNotifications,
            onCheckedChange = onNotificationsChange,
            title = "Feedback notifications",
            description = "Receive short prompts when network quality changes or issues are detected."
        )
        ConsentCheckboxRow(
            checked = consentSync,
            onCheckedChange = onSyncChange,
            title = "Share readings",
            description = "Send readings without your name to help improve network insights (WiFi by default)."
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "You can stop monitoring or change sync settings anytime in Settings.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun PermissionsStepContent(
    permissionsGranted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        SectionHeader(
            title = "App permissions",
            subtitle = "Each permission has a clear purpose. Nothing extra is requested."
        )

        PermissionExplainCard(
            icon = Icons.Default.SignalCellularAlt,
            title = "Phone state",
            purpose = "Read signal strength and cellular network type (2G, 3G, 4G, 5G)."
        )
        PermissionExplainCard(
            icon = Icons.Default.LocationOn,
            title = "Location",
            purpose = "Required by Android to access cell tower signal information. " +
                "We do not track your GPS position."
        )
        PermissionExplainCard(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            purpose = "Show quick rating prompts when we spot network problems."
        )
        PermissionExplainCard(
            icon = Icons.Default.Wifi,
            title = "Network access",
            purpose = "Measure speed and send readings when you are online."
        )

        if (permissionsGranted) {
            Text(
                text = "Permissions granted",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SetupCompleteStepContent(
    deviceIdPreview: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PhoneAndroid,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(36.dp)
            )
        }

        SectionHeader(
            title = "You're ready",
            subtitle = "Setup is complete. No account was created. That is on purpose."
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Your device ID",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = deviceIdPreview,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = "This ID is scrambled before upload. It cannot identify you personally.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Text(
            text = "Next: your dashboard shows network readings. The Feedback tab is where you rate your experience.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HighlightCard(title: String, body: String) {
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun FeatureRow(icon: ImageVector, title: String, body: String) {
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
            Column(modifier = Modifier.padding(start = 14.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ConsentCheckboxRow(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    description: String
) {
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
            Column(modifier = Modifier.padding(start = 4.dp, top = 12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun PermissionExplainCard(icon: ImageVector, title: String, purpose: String) {
    NetLinqCard(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = purpose,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
