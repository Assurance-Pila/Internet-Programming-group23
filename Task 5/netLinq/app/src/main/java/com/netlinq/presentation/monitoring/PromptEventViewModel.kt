package com.netlinq.presentation.monitoring

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.monitoring.NetworkMonitoringManager
import com.netlinq.monitoring.NetworkPromptEvent
import com.netlinq.notifications.FeedbackNotificationHelper
import com.netlinq.presentation.feedback.FeedbackTriggerEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptEventViewModel @Inject constructor(
    private val monitoringManager: NetworkMonitoringManager,
    private val notificationHelper: FeedbackNotificationHelper
) : ViewModel(), DefaultLifecycleObserver {

    private val _foregroundPrompts = MutableSharedFlow<NetworkPromptEvent>(extraBufferCapacity = 1)
    val foregroundPrompts: SharedFlow<NetworkPromptEvent> = _foregroundPrompts.asSharedFlow()

    private var isInForeground = false

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        monitoringManager.start()
        viewModelScope.launch {
            monitoringManager.promptEvents.collect { event ->
                if (isInForeground) {
                    _foregroundPrompts.emit(event)
                } else {
                    notificationHelper.showFeedbackPrompt(
                        trigger = event.trigger,
                        networkType = event.networkType,
                        detail = event.detail,
                        metricId = event.metricId,
                        metricRecordedAt = event.metricRecordedAt,
                        signalStrength = event.signalStrength,
                        latencyMs = event.latencyMs
                    )
                }
            }
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        isInForeground = true
    }

    override fun onStop(owner: LifecycleOwner) {
        isInForeground = false
    }

    companion object {
        fun parsePendingIntent(
            triggerName: String?,
            networkType: String?,
            detail: String?,
            metricId: Long? = null,
            metricRecordedAt: Long? = null,
            signalStrength: Int? = null,
            latencyMs: Int? = null
        ): NetworkPromptEvent? {
            if (triggerName == null) return null
            val trigger = runCatching {
                FeedbackTriggerEvent.valueOf(triggerName)
            }.getOrNull() ?: return null
            return NetworkPromptEvent(
                trigger = trigger,
                networkType = networkType ?: "Unknown",
                detail = detail,
                metricId = metricId,
                metricRecordedAt = metricRecordedAt,
                signalStrength = signalStrength,
                latencyMs = latencyMs
            )
        }
    }
}
