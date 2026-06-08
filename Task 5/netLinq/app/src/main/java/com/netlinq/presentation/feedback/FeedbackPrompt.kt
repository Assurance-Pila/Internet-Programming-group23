package com.netlinq.presentation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SignalCellularAlt
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.StarRatingBar
import kotlinx.coroutines.launch

enum class FeedbackTriggerEvent(
    val title: String,
    val message: String,
    val icon: ImageVector
) {
    SIGNAL_DEGRADATION(
        title = "Signal dropped",
        message = "We noticed weaker signal strength. How did that affect your experience?",
        icon = Icons.Default.SignalCellularAlt
    ),
    NETWORK_TYPE_CHANGE(
        title = "Network changed",
        message = "Your connection switched network type. Did you notice a difference?",
        icon = Icons.Outlined.SwapHoriz
    ),
    LATENCY_SPIKE(
        title = "Internet slowed down",
        message = "Things felt slower than usual. How did apps and pages work?",
        icon = Icons.Outlined.Timer
    ),
    CONNECTIVITY_INTERRUPTION(
        title = "Connection lost",
        message = "You were briefly offline or reconnecting. How disruptive was it?",
        icon = Icons.Default.WifiOff
    )
}

data class FeedbackPromptUiState(
    val trigger: FeedbackTriggerEvent,
    val networkType: String,
    val detail: String? = null,
    val metricId: Long? = null,
    val metricRecordedAt: Long? = null,
    val signalStrength: Int? = null,
    val latencyMs: Int? = null,
    val overallRating: Int = 0,
    val isSubmitting: Boolean = false
) {
    val canSubmit: Boolean = overallRating > 0 && !isSubmitting
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackPromptHost(
    prompt: FeedbackPromptUiState?,
    onDismiss: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onOpenFullFeedback: () -> Unit
) {
    if (prompt == null) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        FeedbackPromptSheet(
            state = prompt,
            onRatingChange = onRatingChange,
            onSubmit = onSubmit,
            onDismiss = {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                }
            },
            onOpenFullFeedback = {
                scope.launch {
                    sheetState.hide()
                    onDismiss()
                    onOpenFullFeedback()
                }
            }
        )
    }
}

@Composable
fun FeedbackPromptSheet(
    state: FeedbackPromptUiState,
    onRatingChange: (Int) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
    onOpenFullFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quick feedback",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "How's your network right now?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "Takes less than 30 seconds · tap a star to rate",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    state.trigger.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Text(text = state.trigger.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = state.trigger.message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = state.detail ?: "On ${networkLabel(state.networkType)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Overall experience",
            style = MaterialTheme.typography.titleMedium
        )
        StarRatingBar(
            rating = state.overallRating,
            onRatingChange = onRatingChange,
            modifier = Modifier.padding(top = 12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.canSubmit
        ) {
            if (state.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Submit")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = onOpenFullFeedback) {
                Text("Rate in detail")
            }
            TextButton(onClick = onDismiss) {
                Text("Not now")
            }
        }
    }
}

private fun networkLabel(type: String): String =
    if (type.equals("unknown", ignoreCase = true)) "your current network" else type
