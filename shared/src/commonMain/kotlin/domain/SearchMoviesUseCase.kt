package domain

import data.model.Movie
import data.repository.MoviesRepository

class SearchMoviesUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke(query: String): List<Movie> {
        return moviesRepository.searchMovies(query)
    }
}