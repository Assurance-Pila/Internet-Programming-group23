package com.netlinq.presentation.operator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.netlinq.presentation.components.NetLinqCard
import com.netlinq.presentation.components.SectionHeader

private data class OperatorSampleRow(
    val networkType: String,
    val avgQoe: String,
    val feedbackCount: Int
)

private val sampleSummary = listOf(
    OperatorSampleRow("4G", "4.1", 128),
    OperatorSampleRow("3G", "2.8", 45),
    OperatorSampleRow("WiFi", "4.5", 89),
    OperatorSampleRow("2G", "1.9", 12)
)

@Composable
fun OperatorAnalyticsScreen() {
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
            title = "Operator analytics (demo)",
            subtitle = "Sample aggregated data for coursework preview. Real operator views will live on a future web dashboard with authenticated access."
        )

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hourly satisfaction summary", style = MaterialTheme.typography.titleMedium)
                Text(
                    "No names shown. Combined totals only.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )
                sampleSummary.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics {
                                contentDescription = "${row.networkType} average rating ${row.avgQoe}"
                            },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(row.networkType, style = MaterialTheme.typography.bodyLarge)
                        Text("Avg ${row.avgQoe}", style = MaterialTheme.typography.bodyMedium)
                        Text("${row.feedbackCount} reports", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }

        NetLinqCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Degradation patterns (sample)", style = MaterialTheme.typography.titleMedium)
                Text("• Signal drops peak 18:00–20:00 in urban zones", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
                Text("• 4G → 3G downgrades correlate with lower satisfaction", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
                Text("• Slow internet above 500 ms: 23% of complaints", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}
