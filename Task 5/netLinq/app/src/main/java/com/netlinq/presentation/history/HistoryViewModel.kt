package com.netlinq.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.repository.NetworkMetricRepository
import com.netlinq.data.repository.QoeFeedbackRepository
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.QoeFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class HistoryUiState(
    val metrics: List<NetworkMetric> = emptyList(),
    val feedback: List<QoeFeedback> = emptyList(),
    val averageQoe: Float = 0f,
    val networkDistribution: Map<String, Int> = emptyMap()
)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    networkMetricRepository: NetworkMetricRepository,
    qoeFeedbackRepository: QoeFeedbackRepository
) : ViewModel() {

    val uiState: StateFlow<HistoryUiState> = combine(
        networkMetricRepository.observeMetrics(),
        qoeFeedbackRepository.observeFeedback()
    ) { metrics, feedback ->
        val avg = if (feedback.isNotEmpty()) {
            feedback.map { it.overallRating }.average().toFloat()
        } else {
            0f
        }
        val distribution = metrics
            .groupingBy { it.networkType.label }
            .eachCount()
        HistoryUiState(
            metrics = metrics.take(30),
            feedback = feedback.take(20),
            averageQoe = avg,
            networkDistribution = distribution
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState()
    )
}
