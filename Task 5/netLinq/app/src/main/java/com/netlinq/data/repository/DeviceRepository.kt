package com.netlinq.data.repository

import com.netlinq.data.preferences.AppPreferences
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val appPreferences: AppPreferences
) {
    val deviceId: Flow<String> = appPreferences.deviceId

    suspend fun getOrCreateDeviceId(): String = appPreferences.ensureDeviceId()

    suspend fun getDeviceHash(): String {
        val rawId = getOrCreateDeviceId()
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(rawId.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
