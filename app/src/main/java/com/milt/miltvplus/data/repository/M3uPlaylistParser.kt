package com.milt.miltvplus.data.repository

import com.milt.miltvplus.domain.model.LiveTvChannel

object M3uPlaylistParser {

    data class ParsedEntry(
        val tvgId: String,
        val name: String,
        val logoUrl: String,
        val category: String,
        val country: String,
        val language: String,
        val streamUrl: String
    )

    val defaultM3uPlaylist: String = """
        #EXTM3U
        #EXTINF:-1 tvg-id="TyCSports.ar" tvg-name="TyC Sports" tvg-logo="https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=400&auto=format&fit=crop" group-title="Sports" tvg-country="Argentina" tvg-language="es",TyC Sports (IPTV Feed 1)
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4
        #EXTINF:-1 tvg-id="TyCSports.ar" tvg-name="TyC Sports" tvg-logo="https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=400&auto=format&fit=crop" group-title="Sports" tvg-country="Argentina" tvg-language="es",TyC Sports (IPTV Feed 2 - Alternative)
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4
        #EXTINF:-1 tvg-id="ESPN.us" tvg-name="ESPN HD" tvg-logo="https://images.unsplash.com/photo-1540747913346-19e32dc3e97e?w=400&auto=format&fit=crop" group-title="Sports" tvg-country="USA" tvg-language="en",ESPN HD (Global Stream)
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4
        #EXTINF:-1 tvg-id="DW.de" tvg-name="DW English" tvg-logo="https://images.unsplash.com/photo-1585829365295-ab7cd400c167?w=400&auto=format&fit=crop" group-title="News" tvg-country="Germany" tvg-language="en",DW English HD
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4
        #EXTINF:-1 tvg-id="France24.fr" tvg-name="France 24 English" tvg-logo="https://images.unsplash.com/photo-1502602898657-3e91760cbb34?w=400&auto=format&fit=crop" group-title="News" tvg-country="France" tvg-language="en",France 24 HD
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4
        #EXTINF:-1 tvg-id="NASA.us" tvg-name="NASA TV" tvg-logo="https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=400&auto=format&fit=crop" group-title="Documentary" tvg-country="USA" tvg-language="en",NASA TV HD
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4
        #EXTINF:-1 tvg-id="RedBullTV.at" tvg-name="Red Bull TV" tvg-logo="https://images.unsplash.com/photo-1517649763962-0c6232669102?w=400&auto=format&fit=crop" group-title="Sports" tvg-country="Austria" tvg-language="en",Red Bull TV
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackSeeTheWorld.mp4
        #EXTINF:-1 tvg-id="TraceUrban.fr" tvg-name="Trace Urban" tvg-logo="https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=400&auto=format&fit=crop" group-title="Music" tvg-country="France" tvg-language="fr",Trace Urban HD
        https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4
    """.trimIndent()

    fun parseM3u(m3uContent: String): List<LiveTvChannel> {
        val lines = m3uContent.lineSequence().map { it.trim() }.filter { it.isNotEmpty() }.toList()
        val allEntries = mutableListOf<ParsedEntry>()
        val seenIds = mutableSetOf<String>()

        var i = 0
        while (i < lines.size) {
            val line = lines[i]
            if (line.startsWith("#EXTINF:", ignoreCase = true)) {
                val extInf = line.substring(8)
                val tvgId = extractAttribute(extInf, "tvg-id")
                val tvgName = extractAttribute(extInf, "tvg-name")
                val logo = extractAttribute(extInf, "tvg-logo").ifEmpty { extractAttribute(extInf, "logo") }
                val group = extractAttribute(extInf, "group-title").ifEmpty { extractAttribute(extInf, "group") }.ifEmpty { "General" }
                val country = extractAttribute(extInf, "tvg-country").ifEmpty { extractAttribute(extInf, "country") }.ifEmpty { "Global" }
                val language = extractAttribute(extInf, "tvg-language").ifEmpty { extractAttribute(extInf, "language") }

                val commaIndex = findSeparatorComma(extInf)
                val titleFromComma = if (commaIndex != -1 && commaIndex < extInf.length - 1) {
                    extInf.substring(commaIndex + 1).trim()
                } else ""

                val channelName = titleFromComma.ifEmpty { tvgName.ifEmpty { tvgId.ifEmpty { "Channel ${allEntries.size + 1}" } } }

                i++
                while (i < lines.size && lines[i].startsWith("#")) {
                    i++
                }

                val streamUrls = mutableListOf<String>()
                while (i < lines.size && !lines[i].startsWith("#")) {
                    val streamUrl = lines[i].trim()
                    if (streamUrl.isNotEmpty()) {
                        streamUrls.add(streamUrl)
                    }
                    i++
                    if (i < lines.size && lines[i].startsWith("#")) break
                }
                i--

                if (streamUrls.isEmpty()) {
                    streamUrls.add("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                }

                for (streamUrl in streamUrls) {
                    allEntries.add(
                        ParsedEntry(
                            tvgId = tvgId.ifEmpty { channelName },
                            name = channelName,
                            logoUrl = logo.ifEmpty { "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=400&auto=format&fit=crop" },
                            category = group,
                            country = country,
                            language = language,
                            streamUrl = streamUrl
                        )
                    )
                }
            }
            i++
        }

        val channels = mutableListOf<LiveTvChannel>()
        for ((index, entry) in allEntries.withIndex()) {
            val baseId = entry.tvgId.ifEmpty { "ch_${index}" }.replace(Regex("[^a-zA-Z0-9_]"), "_")
            val uniqueId = if (seenIds.contains(baseId)) {
                "m3u_${baseId}_$index"
            } else {
                seenIds.add(baseId)
                "m3u_$baseId"
            }

            channels.add(
                LiveTvChannel(
                    id = uniqueId,
                    name = entry.name,
                    logoUrl = entry.logoUrl,
                    category = entry.category,
                    country = entry.country,
                    streamUrl = entry.streamUrl,
                    epgCurrentProgram = "IPTV Stream (${entry.language.ifEmpty { "Live" }})",
                    epgTime = "24/7",
                    epgNextProgram = "Continuous Broadcast",
                    resolution = "1080p HD",
                    isFavorite = false
                )
            )
        }

        return channels
    }

    private fun findSeparatorComma(text: String): Int {
        var inQuotes = false
        var quoteChar = ' '
        for (idx in text.indices) {
            val c = text[idx]
            if ((c == '"' || c == '\'') && (idx == 0 || text[idx - 1] != '\\')) {
                if (!inQuotes) {
                    inQuotes = true
                    quoteChar = c
                } else if (quoteChar == c) {
                    inQuotes = false
                }
            } else if (c == ',' && !inQuotes) {
                return idx
            }
        }
        return text.lastIndexOf(',')
    }

    private fun extractAttribute(text: String, attrName: String): String {
        val regex = Regex("""$attrName\s*=\s*(?:"([^"]*)"|'([^']*)'|([^\s,]*))""", RegexOption.IGNORE_CASE)
        val match = regex.find(text)
        return match?.let {
            it.groupValues[1].ifEmpty { it.groupValues[2].ifEmpty { it.groupValues[3] } }
        } ?: ""
    }
}
