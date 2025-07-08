package data.repository

import core.database.MoviesDatabase
import core.database.entities.toMovie
import core.database.entities.toMovieDetails
import core.database.entities.toMovieDetailsEntity
import core.database.entities.toMovieEntity
import core.network.W3wNetworkDataSource
import core.network.model.NetworkGenre
import core.network.model.NetworkMovieDetails
import core.network.model.NetworkProductionCompany
import core.network.model.NetworkProductionCountry
import core.network.model.NetworkSpokenLanguage
import core.utils.getCurrentTimeMili
import data.model.Genre
import data.model.Movie
import data.model.MovieDetails
import data.model.ProductionCompany
import data.model.ProductionCountry
import data.model.SpokenLanguage

class MoviesRepository(
    private val networkDataSource: W3wNetworkDataSource, private val database: MoviesDatabase
) {
    companion object {
        private const val CACHE_VALIDITY_DURATION = 30 * 60 * 1000L // 30 minutes
        private const val TRENDING_CACHE_TYPE = "trending"
    }

    suspend fun getTrendingMovies(): List<Movie> {
        val validTimestamp = getCurrentTimeMili() - CACHE_VALIDITY_DURATION
        val cachedMovies = database.movieDao().getCachedMovies(TRENDING_CACHE_TYPE, validTimestamp)

        if (cachedMovies.isNotEmpty()) {
            return cachedMovies.map { it.toMovie() }
        }

        return try {
            val networkMovies = networkDataSource.getTrendingMovies()
            val movies = networkMovies.map { networkMovie ->
                Movie(
                    id = networkMovie.id,
                    title = networkMovie.title,
                    releaseDate = networkMovie.releaseDate,
                    voteAverage = networkMovie.voteAverage,
                    posterPath = networkMovie.posterPath,
                    backdropPath = networkMovie.backdropPath
                )
            }

            // Cache the movies
            database.movieDao().clearMoviesByType(TRENDING_CACHE_TYPE)
            database.movieDao().insertMovies(movies.map { it.toMovieEntity(TRENDING_CACHE_TYPE) })

            movies
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return try {
            val networkMovies = networkDataSource.searchMovies(query)
            networkMovies.map { networkMovie ->
                Movie(
                    id = networkMovie.id,
                    title = networkMovie.title,
                    releaseDate = networkMovie.releaseDate,
                    voteAverage = networkMovie.voteAverage,
                    posterPath = networkMovie.posterPath,
                    backdropPath = networkMovie.backdropPath
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMovieDetails(movieId: Int): MovieDetails? {
        val validTimestamp = getCurrentTimeMili() - CACHE_VALIDITY_DURATION
        val cachedMovieDetails = database.movieDao().getCachedMovieDetails(movieId, validTimestamp)

        if (cachedMovieDetails != null) {
            return cachedMovieDetails.toMovieDetails()
        }

        return try {
            val networkMovieDetails = networkDataSource.getMovieDetails(movieId)
            val movieDetails = networkMovieDetails.toMovieDetails()

            // Cache the movie details
            database.movieDao().insertMovieDetails(movieDetails.toMovieDetailsEntity())

            movieDetails
        } catch (e: Exception) {
            null
        }
    }

    suspend fun clearExpiredCache() {
        val expiredTimestamp = getCurrentTimeMili() - CACHE_VALIDITY_DURATION
        database.movieDao().clearExpiredMovies(expiredTimestamp)
        database.movieDao().clearExpiredMovieDetails(expiredTimestamp)
    }

    suspend fun clearCache() {
        database.movieDao().clearAllMovies()
        database.movieDao().clearAllMovieDetails()
    }
}

private fun NetworkMovieDetails.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        posterPath = posterPath,
        backdropPath = backdropPath,
        adult = adult,
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        video = video,
        runtime = runtime,
        budget = budget,
        revenue = revenue,
        status = status,
        tagline = tagline,
        homepage = homepage,
        imdbId = imdbId,
        genres = genres.map { it.toGenre() },
        productionCompanies = productionCompanies.map { it.toProductionCompany() },
        productionCountries = productionCountries.map { it.toProductionCountry() },
        spokenLanguages = spokenLanguages.map { it.toSpokenLanguage() })
}

private fun NetworkGenre.toGenre(): Genre {
    return Genre(id = id, name = name)
}

private fun NetworkProductionCompany.toProductionCompany(): ProductionCompany {
    return ProductionCompany(
        id = id, name = name, logoPath = logoPath, originCountry = originCountry
    )
}

private fun NetworkProductionCountry.toProductionCountry(): ProductionCountry {
    return ProductionCountry(iso31661 = iso31661, name = name)
}

private fun NetworkSpokenLanguage.toSpokenLanguage(): SpokenLanguage {
    return SpokenLanguage(
        englishName = englishName, iso6391 = iso6391, name = name
    )
}