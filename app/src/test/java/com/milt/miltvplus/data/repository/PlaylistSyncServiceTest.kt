package com.milt.miltvplus.data.repository

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PlaylistSyncServiceTest {

    @Test
    fun testPlaylistSyncServiceFallbackAndMetrics() = runTest {
        val service = PlaylistSyncService(null)
        val (channels, metrics) = service.syncPlaylist("https://invalid.url.test.m3u")

        assertTrue(channels.isNotEmpty())
        assertNotNull(metrics)
        assertTrue(metrics.entriesParsed > 0)
        assertTrue(metrics.imported > 0)
        assertTrue(metrics.kept > 0)
        assertNotNull(metrics.countriesDetected)
        assertNotNull(metrics.categoriesDetected)
    }
}
