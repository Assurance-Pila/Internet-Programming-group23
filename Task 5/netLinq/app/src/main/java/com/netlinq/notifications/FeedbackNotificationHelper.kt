package com.netlinq.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.netlinq.MainActivity
import com.netlinq.R
import com.netlinq.presentation.feedback.FeedbackTriggerEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Network feedback",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Quick rating prompts when network quality changes"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showFeedbackPrompt(
        trigger: FeedbackTriggerEvent,
        networkType: String,
        detail: String? = null,
        metricId: Long? = null,
        metricRecordedAt: Long? = null,
        signalStrength: Int? = null,
        latencyMs: Int? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_TRIGGER, trigger.name)
            putExtra(EXTRA_NETWORK_TYPE, networkType)
            detail?.let { putExtra(EXTRA_DETAIL, it) }
            metricId?.let { putExtra(EXTRA_METRIC_ID, it) }
            metricRecordedAt?.let { putExtra(EXTRA_METRIC_RECORDED_AT, it) }
            signalStrength?.let { putExtra(EXTRA_SIGNAL_STRENGTH, it) }
            latencyMs?.let { putExtra(EXTRA_LATENCY_MS, it) }
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            trigger.ordinal,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val body = detail ?: trigger.message
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(trigger.title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(NOTIFICATION_ID_BASE + trigger.ordinal, notification)
    }

    companion object {
        const val CHANNEL_ID = "netlinq_feedback"
        const val EXTRA_TRIGGER = "trigger_event"
        const val EXTRA_NETWORK_TYPE = "network_type"
        const val EXTRA_DETAIL = "trigger_detail"
        const val EXTRA_METRIC_ID = "metric_id"
        const val EXTRA_METRIC_RECORDED_AT = "metric_recorded_at"
        const val EXTRA_SIGNAL_STRENGTH = "signal_strength"
        const val EXTRA_LATENCY_MS = "latency_ms"
        private const val NOTIFICATION_ID_BASE = 1000
    }
}
