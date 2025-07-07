package core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieApiResponse(
    @SerialName("page")
    val page: Int = 0,
    @SerialName("results")
    val results: List<NetworkMovie> = emptyList(),
    @SerialName("total_pages")
    val totalPages: Int = 0,
    @SerialName("total_results")
    val totalResults: Int = 0
)

@Serializable
data class NetworkMovie(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("adult")
    val adult: Boolean = true,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("title")
    val title: String = "",
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("media_type")
    val mediaType: String = "",
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("video")
    val video: Boolean = true,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0
)

