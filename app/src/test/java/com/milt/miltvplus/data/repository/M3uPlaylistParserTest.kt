package com.milt.miltvplus.data.repository

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class M3uPlaylistParserTest {

    @Test
    fun testParseM3uAttributesAndDuplicatePreservation() {
        val sampleM3u = """
            #EXTM3U
            #EXTINF:-1 tvg-id="Test.1" tvg-name="Test Channel 1" tvg-logo="https://logo.png" group-title="Sports" tvg-country="AR" tvg-language="es",Test Channel 1 (Feed A)
            https://stream.example.com/live1.m3u8
            #EXTINF:-1 tvg-id="Test.1" tvg-name="Test Channel 1" tvg-logo="https://logo.png" group-title="Sports" tvg-country="AR" tvg-language="es",Test Channel 1 (Feed B - Duplicate)
            https://stream.example.com/live2.m3u8
        """.trimIndent()

        val channels = M3uPlaylistParser.parseM3u(sampleM3u)
        assertEquals(2, channels.size)

        val ch1 = channels[0]
        assertEquals("Test Channel 1 (Feed A)", ch1.name)
        assertEquals("Sports", ch1.category)
        assertEquals("AR", ch1.country)
        assertEquals("https://logo.png", ch1.logoUrl)
        assertEquals("https://stream.example.com/live1.m3u8", ch1.streamUrl)

        val ch2 = channels[1]
        assertEquals("Test Channel 1 (Feed B - Duplicate)", ch2.name)
        assertEquals("Sports", ch2.category)
        assertEquals("AR", ch2.country)
        assertEquals("https://stream.example.com/live2.m3u8", ch2.streamUrl)
        assertFalse(ch1.id == ch2.id)
    }

    @Test
    fun testParseMalformedLinesAndMissingMetadata() {
        val malformedM3u = """
            #EXTM3U
            Some random comment line
            #EXTINF:-1,Channel Without Metadata
            https://stream.example.com/plain.m3u8
            
            #EXTINF:-1 tvg-id="Composed.Name" group-title="News",News Channel, Extra Info
            https://stream.example.com/news.m3u8
        """.trimIndent()

        val channels = M3uPlaylistParser.parseM3u(malformedM3u)
        assertEquals(2, channels.size)

        assertEquals("Channel Without Metadata", channels[0].name)
        assertEquals("General", channels[0].category)
        assertEquals("Global", channels[0].country)

        assertEquals("News Channel, Extra Info", channels[1].name)
        assertEquals("News", channels[1].category)
    }

    @Test
    fun testMultipleAlternativeUrls() {
        val multiUrlM3u = """
            #EXTM3U
            #EXTINF:-1 tvg-id="Multi.1" tvg-name="Multi Stream Channel" group-title="Entertainment",Channel Multi
            https://stream.example.com/backup1.m3u8
            https://stream.example.com/backup2.m3u8
        """.trimIndent()

        val channels = M3uPlaylistParser.parseM3u(multiUrlM3u)
        assertEquals(2, channels.size)
        assertEquals("https://stream.example.com/backup1.m3u8", channels[0].streamUrl)
        assertEquals("https://stream.example.com/backup2.m3u8", channels[1].streamUrl)
        assertEquals(channels[0].name, channels[1].name)
    }

    @Test
    fun testLargeListPerformance() {
        val sb = StringBuilder()
        sb.append("#EXTM3U\n")
        for (i in 1..2000) {
            sb.append("#EXTINF:-1 tvg-id=\"Ch.$i\" group-title=\"Category${i % 5}\",Channel $i\n")
            sb.append("https://stream.example.com/stream$i.m3u8\n")
        }
        val channels = M3uPlaylistParser.parseM3u(sb.toString())
        assertEquals(2000, channels.size)
        assertEquals("Channel 1", channels[0].name)
        assertEquals("Channel 2000", channels[1999].name)
    }
}
