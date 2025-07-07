package core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkMovieDetails(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("title")
    val title: String = "",
    @SerialName("overview")
    val overview: String = "",
    @SerialName("release_date")
    val releaseDate: String = "",
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    @SerialName("popularity")
    val popularity: Double = 0.0,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("adult")
    val adult: Boolean = false,
    @SerialName("original_language")
    val originalLanguage: String = "",
    @SerialName("original_title")
    val originalTitle: String = "",
    @SerialName("video")
    val video: Boolean = false,
    @SerialName("runtime")
    val runtime: Int? = null,
    @SerialName("budget")
    val budget: Long = 0,
    @SerialName("revenue")
    val revenue: Long = 0,
    @SerialName("status")
    val status: String = "",
    @SerialName("tagline")
    val tagline: String = "",
    @SerialName("homepage")
    val homepage: String = "",
    @SerialName("imdb_id")
    val imdbId: String? = null,
    @SerialName("genres")
    val genres: List<NetworkGenre> = emptyList(),
    @SerialName("production_companies")
    val productionCompanies: List<NetworkProductionCompany> = emptyList(),
    @SerialName("production_countries")
    val productionCountries: List<NetworkProductionCountry> = emptyList(),
    @SerialName("spoken_languages")
    val spokenLanguages: List<NetworkSpokenLanguage> = emptyList()
)

@Serializable
data class NetworkGenre(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = ""
)

@Serializable
data class NetworkProductionCompany(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("logo_path")
    val logoPath: String? = null,
    @SerialName("origin_country")
    val originCountry: String = ""
)

@Serializable
data class NetworkProductionCountry(
    @SerialName("iso_3166_1")
    val iso31661: String = "",
    @SerialName("name")
    val name: String = ""
)

@Serializable
data class NetworkSpokenLanguage(
    @SerialName("english_name")
    val englishName: String = "",
    @SerialName("iso_639_1")
    val iso6391: String = "",
    @SerialName("name")
    val name: String = ""
) 