package presentation.uimodel

import data.model.Movie
import data.model.MovieDetails

data class MoviesUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchMode: Boolean = false,
    val listHeader: String = "Trending movies",
    val selectedMovie: Movie? = null,
    val showMovieDetails: Boolean = false,
    val movieDetails: MovieDetails? = null,
    val isLoadingDetails: Boolean = false,
    val detailsError: String? = null
)