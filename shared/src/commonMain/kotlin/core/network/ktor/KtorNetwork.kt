package core.network.ktor

import core.network.W3wNetworkDataSource
import core.network.model.MovieApiResponse
import core.network.model.NetworkMovie
import core.network.model.NetworkMovieDetails
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private const val W3W_BASE_URL = "https://api.themoviedb.org/3"
private const val TRENDING_MOVIES_ENDPOINT = "$W3W_BASE_URL/trending/movie/day"
private const val SEARCH_MOVIES_ENDPOINT = "$W3W_BASE_URL/search/movie"
private const val MOVIE_DETAILS_ENDPOINT = "$W3W_BASE_URL/movie"

internal class KtorNetwork(private val httpClient: HttpClient) : W3wNetworkDataSource {
    override suspend fun getTrendingMovies(): List<NetworkMovie> {
        return httpClient.get(TRENDING_MOVIES_ENDPOINT).body<MovieApiResponse>().results
    }

    override suspend fun searchMovies(query: String): List<NetworkMovie> {
        return httpClient.get(SEARCH_MOVIES_ENDPOINT) {
            parameter("query", query)
        }.body<MovieApiResponse>().results
    }

    override suspend fun getMovieDetails(movieId: Int): NetworkMovieDetails {
        return httpClient.get("$MOVIE_DETAILS_ENDPOINT/$movieId").body<NetworkMovieDetails>()
    }
}