package com.milt.miltvplus.data.remote.tmdb

import com.milt.miltvplus.data.repository.MoviesRepository
import com.milt.miltvplus.data.repository.SeriesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class TmdbRepositoryTest {

    @Test
    fun testFallbackToLocalMoviesWhenApiFails() = runTest {
        val fakeApiService = object : TmdbApiService({ "test_key" }) {
            override suspend fun getTrendingMovies(apiKeyOverride: String?): Result<List<TmdbMovieDto>> {
                return Result.failure(RuntimeException("API unavailable"))
            }
        }

        val moviesRepo = MoviesRepository()
        val seriesRepo = SeriesRepository()
        val repository = TmdbRepository(fakeApiService, moviesRepo, seriesRepo)

        val items = repository.getTrendingMovies("test_key")
        val expectedMovies = moviesRepo.getMovies().first()
        assertNotNull(items)
        assertEquals(expectedMovies.size, items.size)
        assertEquals(expectedMovies.first().title, items.first().title)
    }

    @Test
    fun testSuccessResponseMapping() = runTest {
        val fakeApiService = object : TmdbApiService({ "test_key" }) {
            override suspend fun getTrendingMovies(apiKeyOverride: String?): Result<List<TmdbMovieDto>> {
                return Result.success(
                    listOf(
                        TmdbMovieDto(
                            id = 999,
                            title = "TMDB Test Movie",
                            overview = "Test Overview",
                            poster_path = "/test.jpg",
                            backdrop_path = "/backdrop.jpg",
                            release_date = "2026-05-01",
                            vote_average = 9.0
                        )
                    )
                )
            }
        }

        val repository = TmdbRepository(fakeApiService)
        val items = repository.getTrendingMovies("test_key")
        assertEquals(1, items.size)
        assertEquals("TMDB Test Movie", items[0].title)
        assertEquals("tmdb_mov_999", items[0].id)
        assertEquals(2026, items[0].releaseYear)
        assertEquals(9.0, items[0].rating, 0.01)
        assertEquals("https://image.tmdb.org/t/p/w500/test.jpg", items[0].posterUrl)
    }

    @Test
    fun missingApiKeyFailsWithoutPretendingToConnect() = runTest {
        val apiService = TmdbApiService { "" }
        val result = apiService.getTrendingMovies()
        assertEquals(true, result.isFailure)
        assertEquals(
            "TMDB_API_KEY no está configurada. Configura la clave de TMDB para habilitar el catálogo en línea.",
            result.exceptionOrNull()?.message
        )
    }

    @Test
    fun testImageUrlHelpers() {
        val apiService = TmdbApiService { "test_key" }
        val posterUrl = apiService.getImageUrl("/abc123.jpg")
        assertEquals("https://image.tmdb.org/t/p/w500/abc123.jpg", posterUrl)

        val defaultPoster = apiService.getImageUrl(null)
        assertNotNull(defaultPoster)

        val backdropUrl = apiService.getBackdropUrl("/backdrop.jpg")
        assertEquals("https://image.tmdb.org/t/p/w1280/backdrop.jpg", backdropUrl)
    }
}
