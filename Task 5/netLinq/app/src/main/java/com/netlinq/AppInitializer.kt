package com.netlinq

import com.netlinq.data.preferences.AppPreferences
import com.netlinq.sync.SyncScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppInitializer @Inject constructor(
    private val appPreferences: AppPreferences,
    private val syncScheduler: SyncScheduler
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun run() {
        scope.launch {
            if (appPreferences.onboardingComplete.first()) {
                val wifiOnly = appPreferences.wifiOnlySync.first()
                syncScheduler.schedulePeriodicSync(wifiOnly = wifiOnly)
            }
        }
    }
}
