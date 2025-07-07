package data.model

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String,
    val voteAverage: Double,
    val posterPath: String? = null,
    val backdropPath: String? = null
)
