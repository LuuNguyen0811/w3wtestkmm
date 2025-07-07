package core.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import data.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey 
    val id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "release_date")
    val releaseDate: String,
    @ColumnInfo(name = "vote_average")
    val voteAverage: Double,
    @ColumnInfo(name = "poster_path")
    val posterPath: String? = null,
    @ColumnInfo(name = "backdrop_path")
    val backdropPath: String? = null,
    @ColumnInfo(name = "cached_timestamp")
    val cachedTimestamp: Long,
    @ColumnInfo(name = "cache_type")
    val cacheType: String // "trending" or "search"
)

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun Movie.toMovieEntity(cacheType: String): MovieEntity {
    return MovieEntity(
        id = id,
        title = title,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath,
        cachedTimestamp = System.currentTimeMillis(),
        cacheType = cacheType
    )
} 