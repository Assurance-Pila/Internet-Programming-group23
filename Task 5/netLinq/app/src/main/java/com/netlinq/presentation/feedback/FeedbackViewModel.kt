package com.netlinq.presentation.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.repository.NetworkMetricRepository
import com.netlinq.data.repository.QoeFeedbackRepository
import com.netlinq.domain.model.FeedbackNetworkLink
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.domain.model.QoeFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FeedbackUiState(
    val overall: Int = 0,
    val responsiveness: Int = 0,
    val streaming: Int = 0,
    val callQuality: Int = 0,
    val satisfaction: Int = 0,
    val notes: String = "",
    val networkContext: String = "Unknown",
    val isSubmitting: Boolean = false,
    val submitted: Boolean = false,
    val error: String? = null
) {
    val isComplete: Boolean =
        overall > 0 && responsiveness > 0 && streaming > 0 && callQuality > 0 && satisfaction > 0
}

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val qoeFeedbackRepository: QoeFeedbackRepository,
    private val networkMetricRepository: NetworkMetricRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkMetricRepository.observeMetrics().collect { metrics ->
                val latest = metrics.firstOrNull()
                _uiState.update {
                    it.copy(networkContext = formatNetworkContext(latest))
                }
            }
        }
    }

    fun setOverall(value: Int) = _uiState.update { it.copy(overall = value, submitted = false) }
    fun setResponsiveness(value: Int) = _uiState.update { it.copy(responsiveness = value, submitted = false) }
    fun setStreaming(value: Int) = _uiState.update { it.copy(streaming = value, submitted = false) }
    fun setCallQuality(value: Int) = _uiState.update { it.copy(callQuality = value, submitted = false) }
    fun setSatisfaction(value: Int) = _uiState.update { it.copy(satisfaction = value, submitted = false) }
    fun setNotes(value: String) = _uiState.update { it.copy(notes = value) }

    fun submitQuickFeedback(
        trigger: FeedbackTriggerEvent,
        overall: Int,
        link: FeedbackNetworkLink?
    ) {
        if (overall <= 0) return
        viewModelScope.launch {
            val resolvedLink = link ?: resolveLatestLink()
            qoeFeedbackRepository.save(
                buildFeedback(
                    overall = overall,
                    responsiveness = overall,
                    streaming = overall,
                    callQuality = overall,
                    satisfaction = overall,
                    triggerEvent = trigger.name.lowercase(),
                    notes = null,
                    link = resolvedLink
                )
            )
        }
    }

    fun submit() {
        val state = _uiState.value
        if (!state.isComplete) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, error = null) }
            try {
                val link = resolveLatestLink()
                qoeFeedbackRepository.save(
                    buildFeedback(
                        overall = state.overall,
                        responsiveness = state.responsiveness,
                        streaming = state.streaming,
                        callQuality = state.callQuality,
                        satisfaction = state.satisfaction,
                        triggerEvent = "manual",
                        notes = state.notes.ifBlank { null },
                        link = link
                    )
                )
                _uiState.update {
                    FeedbackUiState(
                        networkContext = state.networkContext,
                        submitted = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isSubmitting = false, error = e.message ?: "Could not save feedback")
                }
            }
        }
    }

    private suspend fun resolveLatestLink(): FeedbackNetworkLink? {
        val latest = networkMetricRepository.observeMetrics().first().firstOrNull()
        return FeedbackNetworkLink.fromMetric(latest)
    }

    private fun buildFeedback(
        overall: Int,
        responsiveness: Int,
        streaming: Int,
        callQuality: Int,
        satisfaction: Int,
        triggerEvent: String,
        notes: String?,
        link: FeedbackNetworkLink?
    ): QoeFeedback = QoeFeedback(
        overallRating = overall,
        responsivenessRating = responsiveness,
        streamingRating = streaming,
        callQualityRating = callQuality,
        satisfactionRating = satisfaction,
        triggerEvent = triggerEvent,
        networkType = link?.networkType,
        networkMetricId = link?.metricId,
        metricRecordedAt = link?.metricRecordedAt,
        signalStrengthSnapshot = link?.signalStrength,
        latencyMsSnapshot = link?.latencyMs,
        notes = notes,
        recordedAt = System.currentTimeMillis()
    )

    private fun formatNetworkContext(metric: NetworkMetric?): String {
        metric ?: return "No reading yet"
        val signal = metric.signalStrength?.let { formatSignalLabel(it) }
        val speed = metric.latencyMs?.let { "$it ms" }
        return listOfNotNull(metric.networkType.label, signal, speed).joinToString(", ")
    }

    private fun formatSignalLabel(dbm: Int): String = when {
        dbm >= -70 -> "strong signal"
        dbm >= -85 -> "OK signal"
        else -> "weak signal"
    }
}
