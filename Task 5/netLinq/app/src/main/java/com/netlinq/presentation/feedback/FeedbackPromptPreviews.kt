package com.netlinq.presentation.feedback

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.netlinq.ui.theme.NetLinqTheme

@Preview(name = "Prompt · Latency spike", showBackground = true)
@Composable
private fun PreviewFeedbackPromptLatency() {
    NetLinqTheme {
        Surface {
            FeedbackPromptSheet(
                state = FeedbackPromptUiState(
                    trigger = FeedbackTriggerEvent.LATENCY_SPIKE,
                    networkType = "4G",
                    overallRating = 3
                ),
                onRatingChange = {},
                onSubmit = {},
                onDismiss = {},
                onOpenFullFeedback = {}
            )
        }
    }
}

@Preview(name = "Prompt · Signal drop", showBackground = true)
@Composable
private fun PreviewFeedbackPromptSignal() {
    NetLinqTheme {
        Surface {
            FeedbackPromptSheet(
                state = FeedbackPromptUiState(
                    trigger = FeedbackTriggerEvent.SIGNAL_DEGRADATION,
                    networkType = "WiFi",
                    overallRating = 0
                ),
                onRatingChange = {},
                onSubmit = {},
                onDismiss = {},
                onOpenFullFeedback = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "Prompt · Sheet host", showBackground = true, heightDp = 640)
@Composable
private fun PreviewFeedbackPromptHost() {
    NetLinqTheme {
        Surface {
            FeedbackPromptHost(
                prompt = FeedbackPromptUiState(
                    trigger = FeedbackTriggerEvent.NETWORK_TYPE_CHANGE,
                    networkType = "3G",
                    overallRating = 2
                ),
                onDismiss = {},
                onRatingChange = {},
                onSubmit = {},
                onOpenFullFeedback = {}
            )
        }
    }
}
