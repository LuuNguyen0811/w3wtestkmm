package core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import core.database.entities.MovieEntity
import core.database.entities.MovieDetailsEntity

@Dao
interface MovieDao {
    
    // Movie operations
    @Query("SELECT * FROM movies WHERE cache_type = :cacheType AND cached_timestamp > :validTimestamp")
    suspend fun getCachedMovies(cacheType: String, validTimestamp: Long): List<MovieEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    
    @Query("DELETE FROM movies WHERE cache_type = :cacheType")
    suspend fun clearMoviesByType(cacheType: String)
    
    @Query("DELETE FROM movies WHERE cached_timestamp < :expiredTimestamp")
    suspend fun clearExpiredMovies(expiredTimestamp: Long)
    
    // Movie details operations
    @Query("SELECT * FROM movie_details WHERE id = :movieId AND cached_timestamp > :validTimestamp")
    suspend fun getCachedMovieDetails(movieId: Int, validTimestamp: Long): MovieDetailsEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetails: MovieDetailsEntity)
    
    @Query("DELETE FROM movie_details WHERE cached_timestamp < :expiredTimestamp")
    suspend fun clearExpiredMovieDetails(expiredTimestamp: Long)
    
    // Cache management
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    
    @Query("DELETE FROM movie_details")
    suspend fun clearAllMovieDetails()
} 