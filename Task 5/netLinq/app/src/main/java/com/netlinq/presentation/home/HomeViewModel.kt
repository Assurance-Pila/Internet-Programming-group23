package com.netlinq.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.repository.NetworkMetricRepository
import com.netlinq.data.repository.SyncRepository
import com.netlinq.domain.model.NetworkMetric
import com.netlinq.monitoring.NetworkMonitorService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val latestMetric: NetworkMetric? = null,
    val recentMetrics: List<NetworkMetric> = emptyList(),
    val pendingSyncCount: Int = 0,
    val isCollecting: Boolean = false,
    val isSyncing: Boolean = false,
    val statusMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkMonitorService: NetworkMonitorService,
    private val networkMetricRepository: NetworkMetricRepository,
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkMetricRepository.observeMetrics().collect { metrics ->
                _uiState.update {
                    it.copy(
                        latestMetric = metrics.firstOrNull(),
                        recentMetrics = metrics.take(5),
                        pendingSyncCount = metrics.count { m -> !m.synced }
                    )
                }
            }
        }
    }

    fun collectNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isCollecting = true, statusMessage = null) }
            try {
                val metric = networkMonitorService.collectAndSave()
                _uiState.update {
                    it.copy(
                        isCollecting = false,
                        statusMessage = "Saved: ${metric.networkType.label}, ${metric.latencyMs?.let { "$it ms" } ?: "speed not measured"}"
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCollecting = false,
                        statusMessage = "Collection failed: ${e.message}"
                    )
                }
            }
        }
    }

    fun syncNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, statusMessage = null) }
            syncRepository.syncAll()
                .onSuccess { count ->
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            statusMessage = if (count > 0) "Synced $count record(s)" else "Nothing to sync"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            statusMessage = "Sync failed: ${error.message}"
                        )
                    }
                }
        }
    }
}
