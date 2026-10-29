package com.milt.miltvplus.data.remote.tmdb

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

open class TmdbApiService(
    private val apiKeyProvider: () -> String = {
        System.getenv("TMDB_API_KEY")
            ?: System.getProperty("tmdb.api.key")
            ?: "mock_tmdb_api_key_for_testing"
    }
) {
    private val baseUrl = "https://api.themoviedb.org/3"
    private val imageBaseUrl = "https://image.tmdb.org/t/p/w500"
    private val backdropBaseUrl = "https://image.tmdb.org/t/p/w1280"

    open fun getImageUrl(posterPath: String?): String {
        if (posterPath.isNullOrBlank()) return "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop"
        return if (posterPath.startsWith("http")) posterPath else "$imageBaseUrl$posterPath"
    }

    open fun getBackdropUrl(backdropPath: String?): String {
        if (backdropPath.isNullOrBlank()) return "https://images.unsplash.com/photo-1578632767115-351597cf2477?w=1200&auto=format&fit=crop"
        return if (backdropPath.startsWith("http")) backdropPath else "$backdropBaseUrl$backdropPath"
    }

    open suspend fun getTrendingMovies(apiKeyOverride: String? = null): Result<List<TmdbMovieDto>> = withContext(Dispatchers.IO) {
        try {
            val key = apiKeyOverride?.takeIf { it.isNotBlank() } ?: apiKeyProvider()
            val urlString = "$baseUrl/trending/movie/day?api_key=$key"
            val responseString = executeGet(urlString)
            val json = JSONObject(responseString)
            val resultsArray = json.optJSONArray("results") ?: JSONArray()
            val list = mutableListOf<TmdbMovieDto>()
            for (i in 0 until resultsArray.length()) {
                val obj = resultsArray.getJSONObject(i)
                list.add(
                    TmdbMovieDto(
                        id = obj.optInt("id"),
                        title = obj.optString("title").takeIf { it.isNotBlank() },
                        original_title = obj.optString("original_title").takeIf { it.isNotBlank() },
                        overview = obj.optString("overview").takeIf { it.isNotBlank() },
                        poster_path = obj.optString("poster_path").takeIf { it.isNotBlank() },
                        backdrop_path = obj.optString("backdrop_path").takeIf { it.isNotBlank() },
                        release_date = obj.optString("release_date").takeIf { it.isNotBlank() },
                        vote_average = obj.optDouble("vote_average", 0.0),
                        vote_count = obj.optInt("vote_count", 0),
                        popularity = obj.optDouble("popularity", 0.0),
                        adult = obj.optBoolean("adult", false)
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getTrendingTvShows(apiKeyOverride: String? = null): Result<List<TmdbTvDto>> = withContext(Dispatchers.IO) {
        try {
            val key = apiKeyOverride?.takeIf { it.isNotBlank() } ?: apiKeyProvider()
            val urlString = "$baseUrl/trending/tv/day?api_key=$key"
            val responseString = executeGet(urlString)
            val json = JSONObject(responseString)
            val resultsArray = json.optJSONArray("results") ?: JSONArray()
            val list = mutableListOf<TmdbTvDto>()
            for (i in 0 until resultsArray.length()) {
                val obj = resultsArray.getJSONObject(i)
                list.add(
                    TmdbTvDto(
                        id = obj.optInt("id"),
                        name = obj.optString("name").takeIf { it.isNotBlank() },
                        original_name = obj.optString("original_name").takeIf { it.isNotBlank() },
                        overview = obj.optString("overview").takeIf { it.isNotBlank() },
                        poster_path = obj.optString("poster_path").takeIf { it.isNotBlank() },
                        backdrop_path = obj.optString("backdrop_path").takeIf { it.isNotBlank() },
                        first_air_date = obj.optString("first_air_date").takeIf { it.isNotBlank() },
                        vote_average = obj.optDouble("vote_average", 0.0),
                        vote_count = obj.optInt("vote_count", 0),
                        popularity = obj.optDouble("popularity", 0.0)
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun searchMovies(query: String, apiKeyOverride: String? = null): Result<List<TmdbMovieDto>> = withContext(Dispatchers.IO) {
        try {
            val key = apiKeyOverride?.takeIf { it.isNotBlank() } ?: apiKeyProvider()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val urlString = "$baseUrl/search/movie?api_key=$key&query=$encodedQuery"
            val responseString = executeGet(urlString)
            val json = JSONObject(responseString)
            val resultsArray = json.optJSONArray("results") ?: JSONArray()
            val list = mutableListOf<TmdbMovieDto>()
            for (i in 0 until resultsArray.length()) {
                val obj = resultsArray.getJSONObject(i)
                list.add(
                    TmdbMovieDto(
                        id = obj.optInt("id"),
                        title = obj.optString("title").takeIf { it.isNotBlank() },
                        overview = obj.optString("overview").takeIf { it.isNotBlank() },
                        poster_path = obj.optString("poster_path").takeIf { it.isNotBlank() },
                        backdrop_path = obj.optString("backdrop_path").takeIf { it.isNotBlank() },
                        release_date = obj.optString("release_date").takeIf { it.isNotBlank() },
                        vote_average = obj.optDouble("vote_average", 0.0)
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun searchTvShows(query: String, apiKeyOverride: String? = null): Result<List<TmdbTvDto>> = withContext(Dispatchers.IO) {
        try {
            val key = apiKeyOverride?.takeIf { it.isNotBlank() } ?: apiKeyProvider()
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val urlString = "$baseUrl/search/tv?api_key=$key&query=$encodedQuery"
            val responseString = executeGet(urlString)
            val json = JSONObject(responseString)
            val resultsArray = json.optJSONArray("results") ?: JSONArray()
            val list = mutableListOf<TmdbTvDto>()
            for (i in 0 until resultsArray.length()) {
                val obj = resultsArray.getJSONObject(i)
                list.add(
                    TmdbTvDto(
                        id = obj.optInt("id"),
                        name = obj.optString("name").takeIf { it.isNotBlank() },
                        overview = obj.optString("overview").takeIf { it.isNotBlank() },
                        poster_path = obj.optString("poster_path").takeIf { it.isNotBlank() },
                        backdrop_path = obj.optString("backdrop_path").takeIf { it.isNotBlank() },
                        first_air_date = obj.optString("first_air_date").takeIf { it.isNotBlank() },
                        vote_average = obj.optDouble("vote_average", 0.0)
                    )
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun executeGet(urlString: String): String {
        val url = URL(urlString)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = 10_000
            readTimeout = 10_000
            setRequestProperty("Accept", "application/json")
        }
        try {
            val responseCode = connection.responseCode
            if (responseCode !in 200..299) {
                throw RuntimeException("HTTP Error: $responseCode for URL: $urlString")
            }
            BufferedReader(InputStreamReader(connection.inputStream)).use { reader ->
                return reader.readText()
            }
        } finally {
            connection.disconnect()
        }
    }
}
