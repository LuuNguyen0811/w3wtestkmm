package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val adult: Boolean = false,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val video: Boolean = false,
    val runtime: Int? = 0,
    val budget: Long = 0,
    val revenue: Long = 0,
    val status: String = "",
    val tagline: String = "",
    val homepage: String = "",
    val imdbId: String? = null,
    val genres: List<Genre> = emptyList(),
    val productionCompanies: List<ProductionCompany> = emptyList(),
    val productionCountries: List<ProductionCountry> = emptyList(),
    val spokenLanguages: List<SpokenLanguage> = emptyList()
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoPath: String? = null,
    val originCountry: String = ""
)

@Serializable
data class ProductionCountry(
    val iso31661: String,
    val name: String
)

@Serializable
data class SpokenLanguage(
    val englishName: String,
    val iso6391: String,
    val name: String
) 