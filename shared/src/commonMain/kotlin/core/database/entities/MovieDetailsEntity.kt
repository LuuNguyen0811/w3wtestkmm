package core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import data.model.MovieDetails
import data.model.Genre
import data.model.ProductionCompany
import data.model.ProductionCountry
import data.model.SpokenLanguage

@Entity(tableName = "movie_details")
data class MovieDetailsEntity(
    @PrimaryKey 
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "overview")
    val overview: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "vote_count")
    val voteCount: Int,
    @ColumnInfo(name = "popularity")
    val popularity: Double,
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String? = null,
    @ColumnInfo(name = "adult")
    val adult: Boolean = false,
    @ColumnInfo(name = "original_language")
    val originalLanguage: String = "",
    @ColumnInfo(name = "original_title")
    val originalTitle: String = "",
    @ColumnInfo(name = "video")
    val video: Boolean = false,
    @ColumnInfo(name = "runtime")
    val runtime: Int? = null,
    @ColumnInfo(name = "budget")
    val budget: Long = 0,
    @ColumnInfo(name = "revenue")
    val revenue: Long = 0,
    @ColumnInfo(name = "status")
    val status: String = "",
    @ColumnInfo(name = "tagline")
    val tagline: String = "",
    @ColumnInfo(name = "homepage")
    val homepage: String = "",
    @ColumnInfo(name = "imdb_id")
    val imdbId: String? = null,
    @ColumnInfo(name = "genres_json")
    val genresJson: String = "[]",
    @ColumnInfo(name = "production_companies_json")
    val productionCompaniesJson: String = "[]",
    @ColumnInfo(name = "production_countries_json")
    val productionCountriesJson: String = "[]",
    @ColumnInfo(name = "spoken_languages_json")
    val spokenLanguagesJson: String = "[]",
    @ColumnInfo(name = "cached_timestamp")
    val cachedTimestamp: Long
)

fun MovieDetailsEntity.toMovieDetails(): MovieDetails {
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
        genres = parseGenres(genresJson),
        productionCompanies = parseProductionCompanies(productionCompaniesJson),
        productionCountries = parseProductionCountries(productionCountriesJson),
        spokenLanguages = parseSpokenLanguages(spokenLanguagesJson)
    )
}

fun MovieDetails.toMovieDetailsEntity(): MovieDetailsEntity {
    return MovieDetailsEntity(
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
        genresJson = serializeGenres(genres),
        productionCompaniesJson = serializeProductionCompanies(productionCompanies),
        productionCountriesJson = serializeProductionCountries(productionCountries),
        spokenLanguagesJson = serializeSpokenLanguages(spokenLanguages),
        cachedTimestamp = System.currentTimeMillis()
    )
}

// Helper functions for JSON serialization/deserialization
private fun parseGenres(json: String): List<Genre> {
    return try {
        kotlinx.serialization.json.Json.decodeFromString<List<Genre>>(json)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun serializeGenres(genres: List<Genre>): String {
    return try {
        kotlinx.serialization.json.Json.encodeToString(genres)
    } catch (e: Exception) {
        "[]"
    }
}

private fun parseProductionCompanies(json: String): List<ProductionCompany> {
    return try {
        kotlinx.serialization.json.Json.decodeFromString<List<ProductionCompany>>(json)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun serializeProductionCompanies(companies: List<ProductionCompany>): String {
    return try {
        kotlinx.serialization.json.Json.encodeToString(companies)
    } catch (e: Exception) {
        "[]"
    }
}

private fun parseProductionCountries(json: String): List<ProductionCountry> {
    return try {
        kotlinx.serialization.json.Json.decodeFromString<List<ProductionCountry>>(json)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun serializeProductionCountries(countries: List<ProductionCountry>): String {
    return try {
        kotlinx.serialization.json.Json.encodeToString(countries)
    } catch (e: Exception) {
        "[]"
    }
}

private fun parseSpokenLanguages(json: String): List<SpokenLanguage> {
    return try {
        kotlinx.serialization.json.Json.decodeFromString<List<SpokenLanguage>>(json)
    } catch (e: Exception) {
        emptyList()
    }
}

private fun serializeSpokenLanguages(languages: List<SpokenLanguage>): String {
    return try {
        kotlinx.serialization.json.Json.encodeToString(languages)
    } catch (e: Exception) {
        "[]"
    }
} 