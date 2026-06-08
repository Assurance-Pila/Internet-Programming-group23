package com.netlinq.monitoring

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LatencyMeasurer @Inject constructor() {

    suspend fun measure(
        endpoint: String = "https://www.google.com/generate_204"
    ): Int? = withContext(Dispatchers.IO) {
        try {
            val start = System.currentTimeMillis()
            val connection = URL(endpoint).openConnection() as HttpURLConnection
            connection.requestMethod = "HEAD"
            connection.connectTimeout = 5_000
            connection.readTimeout = 5_000
            connection.instanceFollowRedirects = false
            connection.useCaches = false
            connection.connect()
            connection.responseCode
            connection.disconnect()
            (System.currentTimeMillis() - start).toInt()
        } catch (_: Exception) {
            null
        }
    }
}
