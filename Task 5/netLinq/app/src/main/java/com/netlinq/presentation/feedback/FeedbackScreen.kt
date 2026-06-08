package com.netlinq.presentation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.SectionHeader
import com.netlinq.presentation.components.StarRatingRow

@Composable
fun FeedbackScreen(
    uiState: FeedbackUiState,
    onOverallChange: (Int) -> Unit,
    onResponsivenessChange: (Int) -> Unit,
    onStreamingChange: (Int) -> Unit,
    onCallQualityChange: (Int) -> Unit,
    onSatisfactionChange: (Int) -> Unit,
    onNotesChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    if (uiState.submitted) {
        FeedbackSuccessScreen(networkContext = uiState.networkContext)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
            .padding(top = 20.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionHeader(
            title = "Rate your experience",
            subtitle = "Takes under 30 seconds. We may also ask when your network changes."
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Current network: ${uiState.networkContext}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                StarRatingRow(
                    label = "Overall experience",
                    description = "How was your network just now?",
                    rating = uiState.overall,
                    onRatingChange = onOverallChange
                )
                StarRatingRow(
                    label = "Internet responsiveness",
                    description = "Pages, messages, and apps loading quickly?",
                    rating = uiState.responsiveness,
                    onRatingChange = onResponsivenessChange
                )
                StarRatingRow(
                    label = "Streaming",
                    description = "Video and music playback quality?",
                    rating = uiState.streaming,
                    onRatingChange = onStreamingChange
                )
                StarRatingRow(
                    label = "Call quality",
                    description = "Voice and video calls clear?",
                    rating = uiState.callQuality,
                    onRatingChange = onCallQualityChange
                )
                StarRatingRow(
                    label = "General satisfaction",
                    description = "Would you recommend this network right now?",
                    rating = uiState.satisfaction,
                    onRatingChange = onSatisfactionChange
                )
            }
        }

        OutlinedTextField(
            value = uiState.notes,
            onValueChange = onNotesChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Notes (optional)") },
            placeholder = { Text("e.g. buffering on YouTube, dropped call") },
            minLines = 2
        )

        uiState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.isComplete && !uiState.isSubmitting
        ) {
            if (uiState.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Submit feedback")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FeedbackSuccessScreen(networkContext: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.height(64.dp)
        )
        Text(
            text = "Thanks for your feedback!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Your rating on $networkContext helps improve network quality insights across Cameroon.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
