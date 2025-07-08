package data.repository

import core.database.MoviesDatabase
import core.database.dao.MovieDao
import core.database.entities.MovieDetailsEntity
import core.database.entities.MovieEntity
import core.network.W3wNetworkDataSource
import core.network.model.NetworkMovie
import core.network.model.NetworkMovieDetails
import core.utils.getCurrentTimeMili
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class MoviesRepositoryTest {

    private lateinit var repository: MoviesRepository
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource
    private lateinit var fakeDatabase: FakeDatabase
    private lateinit var fakeMovieDao: FakeMovieDao

    @BeforeTest
    fun setup() {
        fakeMovieDao = FakeMovieDao()
        fakeDatabase = FakeDatabase(fakeMovieDao)
        fakeNetworkDataSource = FakeNetworkDataSource()
        repository = MoviesRepository(fakeNetworkDataSource, fakeDatabase)
    }

    @Test
    fun `getTrendingMovies should return cached data when cache is valid`() = runTest {
        // Given
        val cachedMovies = listOf(
            MovieEntity(
                id = 1,
                title = "Cached Movie",
                releaseDate = "2024-01-01",
                voteAverage = 8.5,
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                cachedTimestamp = getCurrentTimeMili() - 1000L, // Recent timestamp
                cacheType = "trending"
            )
        )
        
        fakeMovieDao.cachedMovies = cachedMovies

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertEquals(1, result.size)
        assertEquals("Cached Movie", result[0].title)
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals(0, fakeNetworkDataSource.getTrendingMoviesCallCount)
    }

    @Test
    fun `getTrendingMovies should fetch from network when cache is empty`() = runTest {
        // Given
        val networkMovies = listOf(
            NetworkMovie(
                id = 1,
                title = "Network Movie",
                releaseDate = "2024-01-01",
                voteAverage = 8.5,
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg"
            )
        )
        
        fakeMovieDao.cachedMovies = emptyList()
        fakeNetworkDataSource.trendingMovies = networkMovies

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertEquals(1, result.size)
        assertEquals("Network Movie", result[0].title)
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals(1, fakeNetworkDataSource.getTrendingMoviesCallCount)
        assertTrue(fakeMovieDao.clearMoviesByTypeWasCalled)
        assertTrue(fakeMovieDao.insertMoviesWasCalled)
    }

    @Test
    fun `getTrendingMovies should return empty list when network fails`() = runTest {
        // Given
        fakeMovieDao.cachedMovies = emptyList()
        fakeNetworkDataSource.shouldThrowError = true

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertTrue(result.isEmpty())
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals(1, fakeNetworkDataSource.getTrendingMoviesCallCount)
        assertEquals(0, fakeMovieDao.insertMoviesCallCount)
    }

    @Test
    fun `getTrendingMovies should fetch from network when cache is expired`() = runTest {
        // Given - cache is empty (simulating expired cache)
        val networkMovies = listOf(
            NetworkMovie(
                id = 1,
                title = "Fresh Movie",
                releaseDate = "2024-01-01",
                voteAverage = 8.5,
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg"
            )
        )
        
        fakeMovieDao.cachedMovies = emptyList()
        fakeNetworkDataSource.trendingMovies = networkMovies

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertEquals(1, result.size)
        assertEquals("Fresh Movie", result[0].title)
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals(1, fakeNetworkDataSource.getTrendingMoviesCallCount)
        assertTrue(fakeMovieDao.clearMoviesByTypeWasCalled)
        assertTrue(fakeMovieDao.insertMoviesWasCalled)
    }

    @Test
    fun `getMovieDetails should return cached data when cache is valid`() = runTest {
        // Given
        val movieId = 123
        val cachedMovieDetails = MovieDetailsEntity(
            id = movieId,
            title = "Cached Movie Details",
            overview = "Cached overview",
            releaseDate = "2024-01-01",
            voteAverage = 8.5,
            voteCount = 100,
            popularity = 50.0,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg",
            cachedTimestamp = getCurrentTimeMili() - 1000L // Recent timestamp
        )
        
        fakeMovieDao.cachedMovieDetails = cachedMovieDetails

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result?.id)
        assertEquals("Cached Movie Details", result?.title)
        assertTrue(fakeMovieDao.getCachedMovieDetailsWasCalled)
        assertEquals(0, fakeNetworkDataSource.getMovieDetailsCallCount)
    }

    @Test
    fun `getMovieDetails should fetch from network when cache is empty`() = runTest {
        // Given
        val movieId = 123
        val networkMovieDetails = NetworkMovieDetails(
            id = movieId,
            title = "Network Movie Details",
            overview = "Network overview",
            releaseDate = "2024-01-01",
            voteAverage = 8.5,
            voteCount = 100,
            popularity = 50.0,
            posterPath = "/poster.jpg",
            backdropPath = "/backdrop.jpg"
        )
        
        fakeMovieDao.cachedMovieDetails = null
        fakeNetworkDataSource.movieDetails = networkMovieDetails

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertEquals(movieId, result?.id)
        assertEquals("Network Movie Details", result?.title)
        assertTrue(fakeMovieDao.getCachedMovieDetailsWasCalled)
        assertEquals(1, fakeNetworkDataSource.getMovieDetailsCallCount)
        assertTrue(fakeMovieDao.insertMovieDetailsWasCalled)
    }

    @Test
    fun `getMovieDetails should return null when network fails`() = runTest {
        // Given
        val movieId = 123
        
        fakeMovieDao.cachedMovieDetails = null
        fakeNetworkDataSource.shouldThrowError = true

        // When
        val result = repository.getMovieDetails(movieId)

        // Then
        assertNull(result)
        assertTrue(fakeMovieDao.getCachedMovieDetailsWasCalled)
        assertEquals(1, fakeNetworkDataSource.getMovieDetailsCallCount)
        assertEquals(0, fakeMovieDao.insertMovieDetailsCallCount)
    }

    @Test
    fun `searchMovies should not use cache and always fetch from network`() = runTest {
        // Given
        val query = "action"
        val networkMovies = listOf(
            NetworkMovie(
                id = 1,
                title = "Action Movie",
                releaseDate = "2024-01-01",
                voteAverage = 8.5,
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg"
            )
        )
        
        fakeNetworkDataSource.searchResults = networkMovies

        // When
        val result = repository.searchMovies(query)

        // Then
        assertEquals(1, result.size)
        assertEquals("Action Movie", result[0].title)
        assertEquals(1, fakeNetworkDataSource.searchMoviesCallCount)
        assertEquals(false, fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals(0, fakeMovieDao.insertMoviesCallCount)
    }

    @Test
    fun `searchMovies should return empty list when network fails`() = runTest {
        // Given
        val query = "action"
        fakeNetworkDataSource.shouldThrowError = true

        // When
        val result = repository.searchMovies(query)

        // Then
        assertTrue(result.isEmpty())
        assertEquals(1, fakeNetworkDataSource.searchMoviesCallCount)
    }

    @Test
    fun `clearExpiredCache should clear expired movies and movie details`() = runTest {
        // When
        repository.clearExpiredCache()

        // Then
        assertTrue(fakeMovieDao.clearExpiredMoviesWasCalled)
        assertTrue(fakeMovieDao.clearExpiredMovieDetailsWasCalled)
    }

    @Test
    fun `clearCache should clear all movies and movie details`() = runTest {
        // When
        repository.clearCache()

        // Then
        assertTrue(fakeMovieDao.clearAllMoviesWasCalled)
        assertTrue(fakeMovieDao.clearAllMovieDetailsWasCalled)
    }

    @Test
    fun `cache validation should use correct timestamp calculations`() = runTest {
        // Given - Test that the repository calls the DAO with a timestamp parameter
        fakeMovieDao.cachedMovies = emptyList()
        fakeNetworkDataSource.trendingMovies = emptyList()

        // When
        repository.getTrendingMovies()

        // Then
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertTrue(fakeMovieDao.validTimestampWasPassed)
    }

    @Test
    fun `multiple cache types should be handled independently`() = runTest {
        // Given
        val trendingMovies = listOf(
            MovieEntity(
                id = 1,
                title = "Trending Movie",
                releaseDate = "2024-01-01",
                voteAverage = 8.5,
                posterPath = "/poster.jpg",
                backdropPath = "/backdrop.jpg",
                cachedTimestamp = getCurrentTimeMili() - 1000L,
                cacheType = "trending"
            )
        )
        
        fakeMovieDao.cachedMovies = trendingMovies

        // When
        val result = repository.getTrendingMovies()

        // Then
        assertEquals(1, result.size)
        assertEquals("Trending Movie", result[0].title)
        assertTrue(fakeMovieDao.getCachedMoviesWasCalled)
        assertEquals("trending", fakeMovieDao.lastCacheTypeRequested)
    }
}

// Fake implementations for testing
class FakeNetworkDataSource : W3wNetworkDataSource {
    var trendingMovies: List<NetworkMovie> = emptyList()
    var searchResults: List<NetworkMovie> = emptyList()
    var movieDetails: NetworkMovieDetails? = null
    var shouldThrowError = false
    
    var getTrendingMoviesCallCount = 0
    var searchMoviesCallCount = 0
    var getMovieDetailsCallCount = 0

    override suspend fun getTrendingMovies(): List<NetworkMovie> {
        getTrendingMoviesCallCount++
        if (shouldThrowError) throw Exception("Network error")
        return trendingMovies
    }

    override suspend fun searchMovies(query: String): List<NetworkMovie> {
        searchMoviesCallCount++
        if (shouldThrowError) throw Exception("Network error")
        return searchResults
    }

    override suspend fun getMovieDetails(movieId: Int): NetworkMovieDetails {
        getMovieDetailsCallCount++
        if (shouldThrowError) throw Exception("Network error")
        return movieDetails ?: throw Exception("Movie details not found")
    }
}

class FakeMovieDao : MovieDao {
    var cachedMovies: List<MovieEntity> = emptyList()
    var cachedMovieDetails: MovieDetailsEntity? = null
    
    var getCachedMoviesWasCalled = false
    var getCachedMovieDetailsWasCalled = false
    var insertMoviesWasCalled = false
    var insertMovieDetailsWasCalled = false
    var clearMoviesByTypeWasCalled = false
    var clearExpiredMoviesWasCalled = false
    var clearExpiredMovieDetailsWasCalled = false
    var clearAllMoviesWasCalled = false
    var clearAllMovieDetailsWasCalled = false
    var validTimestampWasPassed = false
    
    var insertMoviesCallCount = 0
    var insertMovieDetailsCallCount = 0
    var lastCacheTypeRequested: String? = null

    override suspend fun getCachedMovies(cacheType: String, validTimestamp: Long): List<MovieEntity> {
        getCachedMoviesWasCalled = true
        lastCacheTypeRequested = cacheType
        validTimestampWasPassed = validTimestamp > 0
        return cachedMovies
    }

    override suspend fun insertMovies(movies: List<MovieEntity>) {
        insertMoviesWasCalled = true
        insertMoviesCallCount++
    }

    override suspend fun clearMoviesByType(cacheType: String) {
        clearMoviesByTypeWasCalled = true
    }

    override suspend fun clearExpiredMovies(expiredTimestamp: Long) {
        clearExpiredMoviesWasCalled = true
    }

    override suspend fun getCachedMovieDetails(movieId: Int, validTimestamp: Long): MovieDetailsEntity? {
        getCachedMovieDetailsWasCalled = true
        validTimestampWasPassed = validTimestamp > 0
        return cachedMovieDetails
    }

    override suspend fun insertMovieDetails(movieDetails: MovieDetailsEntity) {
        insertMovieDetailsWasCalled = true
        insertMovieDetailsCallCount++
    }

    override suspend fun clearExpiredMovieDetails(expiredTimestamp: Long) {
        clearExpiredMovieDetailsWasCalled = true
    }

    override suspend fun clearAllMovies() {
        clearAllMoviesWasCalled = true
    }

    override suspend fun clearAllMovieDetails() {
        clearAllMovieDetailsWasCalled = true
    }
}

class FakeDatabase(private val movieDao: MovieDao) : MoviesDatabase() {
    override fun movieDao(): MovieDao = movieDao

    override fun createInvalidationTracker(): androidx.room.InvalidationTracker {
        return androidx.room.InvalidationTracker(this, emptyMap(), emptyMap(), "movies", "movie_details")
    }
} 