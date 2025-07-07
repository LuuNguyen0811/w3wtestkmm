package core.network

import core.network.model.NetworkMovie
import core.network.model.NetworkMovieDetails

interface W3wNetworkDataSource {
    suspend fun getTrendingMovies(): List<NetworkMovie>
    suspend fun searchMovies(query: String): List<NetworkMovie>
    suspend fun getMovieDetails(movieId: Int): NetworkMovieDetails
}