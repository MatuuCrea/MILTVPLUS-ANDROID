package com.milt.miltvplus.data.repository

import android.content.Context
import com.milt.miltvplus.domain.model.LiveTvChannel
import java.io.File
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request

data class SyncMetrics(
    val url: String,
    val success: Boolean,
    val errorMessage: String? = null,
    val entriesParsed: Int = 0,
    val imported: Int = 0,
    val kept: Int = 0,
    val rejected: Int = 0,
    val countriesDetected: List<String> = emptyList(),
    val categoriesDetected: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

class PlaylistSyncService(private val context: Context? = null) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    suspend fun syncPlaylist(url: String): Pair<List<LiveTvChannel>, SyncMetrics> {
        return withContext(Dispatchers.IO) {
            var rawContent: String? = null
            var success = false
            var errorMessage: String? = null

            try {
                withTimeout(15000L) {
                    val request = Request.Builder().url(url).build()
                    client.newCall(request).execute().use { response ->
                        if (response.isSuccessful) {
                            rawContent = response.body?.string()
                            if (!rawContent.isNullOrBlank()) {
                                success = true
                                context?.let { saveToCache(it, url, rawContent!!) }
                            } else {
                                errorMessage = "Response body is empty"
                            }
                        } else {
                            errorMessage = "HTTP error code: ${response.code}"
                        }
                    }
                }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: e.javaClass.simpleName
            }

            // Fallback protection: load from local cache if download failed
            if (!success && context != null) {
                rawContent = loadFromCache(context, url)
                if (!rawContent.isNullOrBlank()) {
                    errorMessage = "$errorMessage (Loaded from local cache fallback)"
                }
            }

            // Fallback protection: if still empty, use default M3U playlist
            if (rawContent.isNullOrBlank()) {
                rawContent = M3uPlaylistParser.defaultM3uPlaylist
                errorMessage = "$errorMessage (Using default static M3U playlist fallback)"
            }

            val parsedChannels = try {
                M3uPlaylistParser.parseM3u(rawContent ?: "")
            } catch (e: Exception) {
                emptyList()
            }

            val lines = (rawContent ?: "").lineSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
            var totalEntriesParsed = 0
            for (line in lines) {
                if (line.startsWith("#EXTINF:", ignoreCase = true)) {
                    totalEntriesParsed++
                }
            }

            val importedCount = parsedChannels.size
            val rejectedCount = maxOf(0, totalEntriesParsed - importedCount)
            val keptCount = importedCount

            val countries = parsedChannels.map { it.country }
                .filter { it.isNotBlank() && !it.equals("Global", ignoreCase = true) }
                .distinct()
                .sorted()

            val categories = parsedChannels.map { it.category }
                .filter { it.isNotBlank() }
                .distinct()
                .sorted()

            val metrics = SyncMetrics(
                url = url,
                success = success || importedCount > 0,
                errorMessage = errorMessage,
                entriesParsed = totalEntriesParsed,
                imported = importedCount,
                kept = keptCount,
                rejected = rejectedCount,
                countriesDetected = countries,
                categoriesDetected = categories
            )

            Pair(parsedChannels, metrics)
        }
    }

    private fun saveToCache(context: Context, url: String, content: String) {
        try {
            val cacheFile = File(context.cacheDir, "m3u_cache_${url.hashCode()}.m3u")
            cacheFile.writeText(content)
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun loadFromCache(context: Context, url: String): String? {
        return try {
            val cacheFile = File(context.cacheDir, "m3u_cache_${url.hashCode()}.m3u")
            if (cacheFile.exists()) cacheFile.readText() else null
        } catch (e: Exception) {
            null
        }
    }
}
